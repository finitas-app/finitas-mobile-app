package pl.finitas.app.sync_feature.domain

import pl.finitas.app.core.data.model.MessagesVersion
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.core.data.model.User
import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.UserIdValue
import pl.finitas.app.core.domain.repository.MessageSenderRepository
import pl.finitas.app.core.domain.repository.SendMessageRequest
import pl.finitas.app.core.domain.repository.SingleMessageDto
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.sync_feature.data.data_source.NewMessagesResponse
import pl.finitas.app.sync_feature.domain.repository.MessageSyncRepository
import pl.finitas.app.sync_feature.domain.repository.RoomSyncRepository
import pl.finitas.app.sync_feature.domain.repository.UserRepository
import pl.finitas.app.sync_feature.domain.repository.VersionsRepository
import java.util.UUID

class SynchronizationService(
    private val messageSyncRepository: MessageSyncRepository,
    private val versionsRepository: VersionsRepository,
    private val roomSyncRepository: RoomSyncRepository,
    private val messageSenderRepository: MessageSenderRepository,
    private val userStoreRepository: UserStoreRepository,
    private val userRepository: UserRepository,
) {
    suspend fun fullSync(authorizedUserId: UUID) {
        fullSyncRooms(authorizedUserId)
        fullSyncNames()
        fullSyncMessages(authorizedUserId)
    }

    // TODO: split for room parts and sync only room that is needed
    suspend fun fullSyncRooms(authorizedUserId: UUID) {
        val roomVersions = versionsRepository.getRoomVersions()
        val messagesVersion = versionsRepository.getMessagesVersions().map { it.idRoom }
        val (remoteRooms, unavailableRooms) = roomSyncRepository.getRoomsFromVersionRemote(roomVersions)
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
            fullSyncNames()
        }
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
            versionsRepository.setMessagesVersion(MessagesVersion(idRoom, sortedMessagesByVersion.last().version))
        }
    }

    private suspend fun fullSyncNames() {
        userRepository.getUserIds()
            .let { idsUser ->
                userStoreRepository.getVisibleNames(GetVisibleNamesRequest(idsUser.map { UserIdValue(it) }))
            }
            .let { users ->
                userRepository.saveUsers(users.map { User(idUser = it.idUser, username = it.visibleName) })
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
        }catch (_: Exception) {}

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
}