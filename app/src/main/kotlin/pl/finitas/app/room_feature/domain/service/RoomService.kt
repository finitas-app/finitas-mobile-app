package pl.finitas.app.room_feature.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.room_feature.domain.RoomPreviewDto
import pl.finitas.app.room_feature.domain.repository.MessageRepository
import pl.finitas.app.room_feature.domain.repository.RoomRepository
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

    suspend fun getRoomById(idRoom: UUID): Room? {
        return roomRepository.getRoomById(idRoom)
    }
}