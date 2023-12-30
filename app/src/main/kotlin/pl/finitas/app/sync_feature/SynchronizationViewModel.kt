package pl.finitas.app.sync_feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                // TODO: replace with flow changes
                val authToken = profileRepository.getAuthToken().first()
                if (authToken == null) {
                    delay(2000)
                    continue
                }
                try {
                    httpClient.webSocket({
                        url(HttpUrls.synchronizationWebsocket)
                        bearerAuth(authToken)
                    }) {
                        val authorizedUserId = profileRepository.getAuthorizedUserId().first()
                            ?: throw UnauthorizedUserException()
                        with(synchronizationService) {
                            this@launch.fullSync(authorizedUserId)
                        }
                        for (message in incoming) {
                            message as? Frame.Text ?: continue

                            val userNotification: UserNotification = Json.decodeFromString(
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

    private suspend fun proceedEvent(
        userNotification: UserNotification,
        authorizedUserId: UUID,
    ) = with(userNotification) {
        when {
            event == UserNotificationEvent.SYNC_MESSAGE && jsonData != null -> {
                synchronizationService.syncMessages(
                    authorizedUserId,
                    Json.decodeFromString(jsonData),
                )
            }
            event == UserNotificationEvent.SYNC_ROOM && jsonData == null -> {
                synchronizationService.fullSyncRooms(authorizedUserId)
            }
            event == UserNotificationEvent.USERNAME_CHANGE && jsonData != null -> {
                val data = Json.decodeFromString<UserIdValue>(jsonData)
                // TODO: Send username using websocket
                synchronizationService.fullSyncNames(listOf(data))
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
}