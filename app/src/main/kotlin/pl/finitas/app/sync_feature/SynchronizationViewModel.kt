package pl.finitas.app.sync_feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pl.finitas.app.core.domain.dto.store.UserIdValue
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.http.HttpUrls
import pl.finitas.app.room_feature.domain.service.UnauthorizedUserException
import pl.finitas.app.sync_feature.domain.SynchronizationService
import java.net.ConnectException
import java.util.UUID

class SynchronizationViewModel(
    private val profileRepository: ProfileRepository,
    private val synchronizationService: SynchronizationService,
    private val httpClient: HttpClient,
) : ViewModel() {

    init {
        println("Synchronization task start")
        var connection: DefaultClientWebSocketSession? = null
        var job: Job? = null
        viewModelScope.launch(Dispatchers.Default) {
            // TODO: replace with flow changes
            profileRepository.getAuthToken().collect { authToken ->
                println("Retry with token: ${authToken?.take(30)}")
                httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
                    .firstOrNull()?.clearToken()
                connection?.close()
                connection = null
                job?.cancel()
                if (authToken != null) {
                    job = launch(Dispatchers.Default) {
                        val coroutineScope = this
                        while (this.isActive) {
                            connection?.close()
                            try {
                                httpClient.webSocket({
                                    url(HttpUrls.synchronizationWebsocket)
                                    bearerAuth(authToken)
                                }) {
                                    connection = this
                                    val authorizedUserId =
                                        profileRepository.getAuthorizedUserId().first()
                                            ?: throw UnauthorizedUserException()
                                    with(synchronizationService) {
                                        coroutineScope.fullSync(authorizedUserId)
                                    }
                                    for (message in incoming) {
                                        message as? Frame.Text ?: continue

                                        val userNotification: UserNotification =
                                            Json.decodeFromString(
                                                message.readText()
                                            )
                                        proceedEvent(userNotification, authorizedUserId)
                                    }
                                }
                            } catch (_: ConnectException) {
                                delay(5000)
                            } catch (e: Exception) {
                                delay(1000)
                                e.printStackTrace()
                            }
                        }
                    }
                }

            }
        }
    }

    private fun CoroutineScope.proceedEvent(
        userNotification: UserNotification,
        authorizedUserId: UUID,
    ) = launch {
        with(userNotification) {
            when {
                event == UserNotificationEvent.SYNC_MESSAGE && jsonData != null -> {
                    synchronizationService.syncMessages(authorizedUserId, Json.decodeFromString(jsonData))
                }

                event == UserNotificationEvent.SYNC_ROOM && jsonData == null -> {
                    synchronizationService.fullSyncRooms(authorizedUserId)
                }

                event == UserNotificationEvent.USERNAME_CHANGE && jsonData != null -> {
                    val data = Json.decodeFromString<UserIdValue>(jsonData)
                    // TODO: Send username using websocket
                    synchronizationService.fullSyncNames(listOf(data))
                }

                event == UserNotificationEvent.CATEGORY_CHANGED && jsonData != null -> {
                    val data = Json.decodeFromString<UserIdValue>(jsonData)
                    // TODO: Send categories using websocket or optimize providing user id
                    synchronizationService.retrieveNewCategories(authorizedUserId)
                }

                event == UserNotificationEvent.SHOPPING_LIST_CHANGED -> {
                    // TODO: Send categories using websocket or optimize providing user id
                    synchronizationService.retrieveNewShoppingLists(authorizedUserId)
                }

                event == UserNotificationEvent.REGENERATE_INVITATION_LINK -> {
                    synchronizationService.fullSyncRooms(authorizedUserId)
                }

                event == UserNotificationEvent.CHANGE_ROOM_NAME -> {
                    synchronizationService.fullSyncRooms(authorizedUserId)
                }

                event == UserNotificationEvent.ADD_USER_TO_ROOM -> {
                    synchronizationService.fullSyncRooms(authorizedUserId)
                    synchronizationService.fullSyncNames(listOf())
                    synchronizationService.retrieveNewCategories(authorizedUserId)
                    synchronizationService.retrieveNewShoppingLists(authorizedUserId)
                    synchronizationService.retrieveNewFinishedSpendings(authorizedUserId)
                }

                event == UserNotificationEvent.DELETE_USER_FROM_ROOM -> {
                    synchronizationService.fullSyncRooms(authorizedUserId)
                    // TODO: Send deleted user id using websocket
                }

                event == UserNotificationEvent.ASSIGN_ROLE_TO_USER -> {
                    // TODO: Optimize with provided new role for user
                    synchronizationService.fullSyncRooms(authorizedUserId)
                    // TODO: run only if new role
                    synchronizationService.retrieveNewFinishedSpendings(authorizedUserId)
                }

                event == UserNotificationEvent.FINISHED_SPENDING_CHANGED -> {
                    synchronizationService.retrieveNewFinishedSpendings(authorizedUserId)
                }
            }
        }
    }
}

@Serializable
private data class UserNotification(
    val event: UserNotificationEvent,
    val jsonData: String?,
)

enum class UserNotificationEvent {
    SYNC_MESSAGE,
    SYNC_ROOM,
    USERNAME_CHANGE,
    CATEGORY_CHANGED,
    SHOPPING_LIST_CHANGED,
    REGENERATE_INVITATION_LINK,
    CHANGE_ROOM_NAME,
    ADD_USER_TO_ROOM,
    DELETE_USER_FROM_ROOM,
    ASSIGN_ROLE_TO_USER,
    FINISHED_SPENDING_CHANGED,
}