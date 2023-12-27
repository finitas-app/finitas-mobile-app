package pl.finitas.app.room_feature.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.room_feature.domain.AddRoomDto
import pl.finitas.app.room_feature.domain.RoomPreviewDto
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import pl.finitas.app.room_feature.domain.repository.AddRoleRequest
import pl.finitas.app.room_feature.domain.repository.DeleteRoleRequest
import pl.finitas.app.room_feature.domain.repository.MessageRepository
import pl.finitas.app.room_feature.domain.repository.RoomRepository
import pl.finitas.app.room_feature.domain.repository.UpdateRoleRequest
import pl.finitas.app.room_feature.presentation.room_settings.roles.UpsertRoleState
import pl.finitas.app.room_feature.presentation.rooms.AddRoomState
import java.util.UUID

class RoomService(
    private val roomRepository: RoomRepository,
    private val messageRepository: MessageRepository,
) {
    fun getRoomsPreview(): Flow<List<RoomPreviewDto>> = roomRepository.getRooms().combine(
        messageRepository
            .getLastMessage()
    ) { rooms, messages ->
        val roomIds = rooms.map { it.idRoom }
        val lastMessagesByRoom = messages
            .groupBy { it.idRoom }
            .mapValues { it.value[0] }
        val unreadCountByRoom = messageRepository
            .getUnreadMessageCountBy(roomIds)
            .groupBy { it.idRoom }
            .mapValues { it.value[0] }

        rooms.map {
            RoomPreviewDto(
                idRoom = it.idRoom,
                title = it.title,
                lastMessage = lastMessagesByRoom[it.idRoom]?.content ?: "",
                unreadMessagesNumber = unreadCountByRoom[it.idRoom]?.unreadCount ?: 0
            )
        }
    }

    suspend fun getRoomById(idRoom: UUID): Room {
        return roomRepository.getRoomById(idRoom)
    }

    fun getRoomWithAdditionalInfo(idRoom: UUID): Flow<RoomWithAdditionalInfoView> {
        return roomRepository.getRoomWithAdditionalInfo(idRoom)
    }

    suspend fun addRoom(addRoomState: AddRoomState) {
        roomRepository.addRoomRepository(addRoomState.toDto())
    }

    suspend fun upsertRole(idRoom: UUID, upsertRoleState: UpsertRoleState) {
        if (upsertRoleState.idRole == null)
            roomRepository.addRole(
                AddRoleRequest(
                    idRoom = idRoom,
                    name = upsertRoleState.name,
                    authorities = upsertRoleState.authorities,
                )
            )
        else
            roomRepository.updateRole(
                UpdateRoleRequest(
                    idRole = upsertRoleState.idRole,
                    idRoom = idRoom,
                    name = upsertRoleState.name,
                    authorities = upsertRoleState.authorities,
                )
            )
    }

    suspend fun deleteRole(idRoom: UUID, idRole: UUID) {
        roomRepository.deleteRole(
            DeleteRoleRequest(idRoom, idRole)
        )
    }
}

private fun AddRoomState.toDto() = AddRoomDto(title)