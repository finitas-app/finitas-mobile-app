package pl.finitas.app.sync_feature.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import pl.finitas.app.core.data.model.MessagesVersion
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.core.data.model.ShoppingListVersion
import pl.finitas.app.core.data.model.User
import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.RemoteShoppingItemDto
import pl.finitas.app.core.domain.dto.store.RemoteShoppingListDto
import pl.finitas.app.core.domain.dto.store.RemoteSpendingRecordDataDto
import pl.finitas.app.core.domain.dto.store.SynchronizationRequest
import pl.finitas.app.core.domain.dto.store.UserIdValue
import pl.finitas.app.core.domain.repository.MessageSenderRepository
import pl.finitas.app.core.domain.repository.SendMessageRequest
import pl.finitas.app.core.domain.repository.ShoppingListStoreRepository
import pl.finitas.app.core.domain.repository.SingleMessageDto
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemDto
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListDto
import pl.finitas.app.sync_feature.data.data_source.NewMessagesResponse
import pl.finitas.app.sync_feature.domain.repository.MessageSyncRepository
import pl.finitas.app.sync_feature.domain.repository.RoomSyncRepository
import pl.finitas.app.sync_feature.domain.repository.ShoppingListSyncRepository
import pl.finitas.app.sync_feature.domain.repository.UserRepository
import pl.finitas.app.sync_feature.domain.repository.VersionsRepository
import java.util.UUID

// TODO Split the whole file into use cases instead of methods since it will be very large in size
class SynchronizationService(
    private val messageSyncRepository: MessageSyncRepository,
    private val versionsRepository: VersionsRepository,
    private val roomSyncRepository: RoomSyncRepository,
    private val messageSenderRepository: MessageSenderRepository,
    private val userStoreRepository: UserStoreRepository,
    private val userRepository: UserRepository,
    private val shoppingListSyncRepository: ShoppingListSyncRepository,
    private val shoppingListStoreRepository: ShoppingListStoreRepository,
) {
    fun CoroutineScope.fullSync(authorizedUserId: UUID) = launch {
        fullSyncRooms(authorizedUserId)
        fullSyncNames(listOf())
        fullSyncMessages(authorizedUserId)
        //fullSyncShoppingLists(authorizedUserId)
    }

    // TODO: split for room parts and sync only room that is needed
    suspend fun fullSyncRooms(authorizedUserId: UUID) {
        val roomVersions = versionsRepository.getRoomVersions()
        val messagesVersion = versionsRepository.getMessagesVersions().map { it.idRoom }
        val (remoteRooms, unavailableRooms) = roomSyncRepository.getRoomsFromVersionRemote(
            roomVersions
        )
        roomSyncRepository.deleteRooms(unavailableRooms)
        roomSyncRepository.upsertRooms(remoteRooms)

        versionsRepository.setRoomVersions(remoteRooms.map { RoomVersion(it.idRoom, it.version) })
        val newMessageVersions = remoteRooms.filter { it.idRoom !in messagesVersion }.map {
            MessagesVersion(it.idRoom, -1)
        }
        if (newMessageVersions.isNotEmpty()) {
            // TODO: After part split left it only for create_event or join_event
            versionsRepository.setMessagesVersions(newMessageVersions)
            fullSyncMessages(authorizedUserId, newMessageVersions)
        }
        // TODO: Left it only for new user added
        fullSyncNames(listOf())
    }

    suspend fun syncMessages(authorizedUserId: UUID, newMessages: List<NewMessagesResponse>) {
        val messagesGroupedByIdRoom = newMessages.groupBy { it.idRoom }
        for ((idRoom, messages) in messagesGroupedByIdRoom) {
            val sortedMessagesByVersion = messages.sortedBy { it.version }
            val currentMessageVersion = versionsRepository.getMessagesVersion(idRoom)
            if (currentMessageVersion.version + 1 != sortedMessagesByVersion[0].version) {
                fullSyncMessages(authorizedUserId)
                break
            }
            messageSyncRepository.upsertMessages(
                sortedMessagesByVersion.map {
                    RoomMessage(
                        idUser = it.idUser,
                        isPending = false,
                        idRoom = it.idRoom,
                        idMessage = it.idMessage,
                        idShoppingList = it.idShoppingList,
                        content = it.content,
                        createdAt = it.createdAt,
                        isRead = authorizedUserId == it.idUser,
                        version = it.version,
                    )
                }
            )
            versionsRepository.setMessagesVersion(
                MessagesVersion(
                    idRoom,
                    sortedMessagesByVersion.last().version
                )
            )
        }
    }

    suspend fun fullSyncNames(names: List<UserIdValue>) {
        userStoreRepository.getVisibleNames(GetVisibleNamesRequest(names))
            .let { users ->
                userRepository.saveUsers(users.map { User(idUser = it.idUser, username = it.visibleName ?: "") })
            }
    }

    private suspend fun fullSyncMessages(
        authorizedUserId: UUID,
        messageVersionsFilter: List<MessagesVersion>? = null,
    ) {
        val messageVersions = messageVersionsFilter ?: versionsRepository.getMessagesVersions()
        val response = messageSyncRepository.getMessagesFromVersionRemote(messageVersions)
        roomSyncRepository.deleteRooms(response.unavailableRooms)
        val pendingMessages = messageSyncRepository.getPendingMessages()
        try {
            messageSenderRepository.sendMessages(
                SendMessageRequest(
                    messages = pendingMessages.map {
                        SingleMessageDto(
                            idMessage = it.idMessage,
                            idRoom = it.idRoom,
                            content = it.content,
                            idShoppingList = it.idShoppingList,
                        )
                    }
                )
            )
        } catch (_: Exception) {
        }

        val messagesByIdRoom = response.messages.groupBy { it.idRoom }
        messageSyncRepository.upsertMessages(
            response.messages.map {
                RoomMessage(
                    idUser = it.idUser,
                    isPending = false,
                    idRoom = it.idRoom,
                    idMessage = it.idMessage,
                    idShoppingList = it.idShoppingList,
                    content = it.content,
                    createdAt = it.createdAt,
                    isRead = it.idUser == authorizedUserId,
                    version = it.version,
                )
            }
        )
        versionsRepository.setMessagesVersions(
            messagesByIdRoom.map { entry ->
                MessagesVersion(
                    idRoom = entry.key,
                    version = entry.value.maxOf { it.version },
                )
            }
        )
    }

    private fun CoroutineScope.fullSyncShoppingLists(authorizedUserId: UUID) = launch {
        var versions = shoppingListSyncRepository.getShoppingListVersions()
        val forSyncShoppingListByIdUser = shoppingListSyncRepository
            .getNotSynchronizedShoppingLists()
            .groupBy { it.idUser }
        if (versions.find { it.idUser == authorizedUserId } == null) {
            val currentUserVersions = ShoppingListVersion(authorizedUserId, -1)
            shoppingListSyncRepository.setShoppingListVersion(currentUserVersions)
            versions = versions + currentUserVersions
        }

        val responses = versions.map { (idUser, version) ->
            idUser to async {
                shoppingListStoreRepository.synchronizeShoppingLists(
                    SynchronizationRequest(
                        lastSyncVersion = version,
                        idUser = idUser,
                        objects = forSyncShoppingListByIdUser[
                            if (idUser == authorizedUserId) null else idUser
                        ]?.map { it.toRemote(idUser) } ?: listOf(),
                    )
                )
            }
        }
        // TODO: Add unavailable users ids to response
        val result = responses
            .map { it.first }
            .zip(responses.map { it.second }.awaitAll())
            .filter { it.second.objects.isNotEmpty() }

        result.forEach { (idUser, response) ->
            response.objects.forEach {
                shoppingListSyncRepository.upsertShoppingList(it.toDto(authorizedUserId))
            }
            shoppingListSyncRepository.setShoppingListVersion(
                ShoppingListVersion(
                    idUser,
                    response.actualizedSyncVersion
                )
            )
        }
        versions.forEach { (idUser, version) ->
            if (idUser == authorizedUserId) {
                shoppingListSyncRepository.deleteCurrentUserMarkedShoppingListUnderVersion(version)
            } else {
                shoppingListSyncRepository.deleteMarkedShoppingListUnderVersion(ShoppingListVersion(idUser, version))
            }
        }
    }
}

private fun ShoppingListDto.toRemote(idUser: UUID) = RemoteShoppingListDto(
    idShoppingList = idShoppingList,
    shoppingItems = shoppingItems.map {
        RemoteShoppingItemDto(
            idShoppingItem = it.idSpendingRecordData,
            amount = it.amount,
            spendingRecordData = RemoteSpendingRecordDataDto(
                idSpendingRecordData = it.idSpendingRecordData,
                name = it.name,
                idCategory = it.idSpendingCategory,
            )
        )
    },
    version = -1,
    idUser = idUser,
    isDeleted = isDeleted,
    name = name,
    color = color,
    isFinished = isFinished,
)

private fun RemoteShoppingListDto.toDto(authorizedUserId: UUID) = ShoppingListDto(
    name = name,
    color = color,
    idShoppingList = idShoppingList,
    shoppingItems = shoppingItems.map {
        ShoppingItemDto(
            idSpendingRecordData = it.spendingRecordData.idSpendingRecordData,
            name = it.spendingRecordData.name,
            idSpendingCategory = it.spendingRecordData.idCategory,
            amount = it.amount,
        )
    },
    isFinished = isFinished,
    isDeleted = isDeleted,
    version = version,
    idUser = if (idUser == authorizedUserId) null else idUser,
)

















