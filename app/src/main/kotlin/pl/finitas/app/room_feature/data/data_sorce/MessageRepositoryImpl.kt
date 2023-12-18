package pl.finitas.app.room_feature.data.data_sorce

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.MessageDao
import pl.finitas.app.core.data.data_source.dao.UnreadMessageCountByRoom
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.room_feature.domain.repository.MessageRepository
import java.util.UUID

class MessageRepositoryImpl(
    private val messageDao: MessageDao,
) : MessageRepository {
    override fun getLastMessage(): Flow<List<RoomMessage>> {
        return messageDao.getLastMessages()
    }

    override suspend fun getUnreadMessageCountBy(idsRoom: List<UUID>): List<UnreadMessageCountByRoom> {
        return messageDao.getUnreadMessageCountBy(idsRoom)
    }

    override fun getMessagesByIdRoom(idRoom: UUID): Flow<List<RoomMessage>> {
        return messageDao.getMessagesByIdRoomFlow(idRoom)
    }

    override suspend fun saveMessage(roomMessage: RoomMessage) {
        messageDao.upsertMessages(listOf(roomMessage))
    }
}