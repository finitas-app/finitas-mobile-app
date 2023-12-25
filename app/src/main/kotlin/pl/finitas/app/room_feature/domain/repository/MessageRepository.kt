package pl.finitas.app.room_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.UnreadMessageCountByRoom
import pl.finitas.app.core.data.model.RoomMessage
import java.util.UUID

interface MessageRepository {

    fun getLastMessage(): Flow<List<RoomMessage>>

    suspend fun getUnreadMessageCountBy(idsRoom: List<UUID>): List<UnreadMessageCountByRoom>

    fun getMessagesByIdRoom(idRoom: UUID): Flow<List<RoomMessage>>

    suspend fun saveMessage(roomMessage: RoomMessage)
    suspend fun readMessage(idsMessage: List<UUID>)
}

