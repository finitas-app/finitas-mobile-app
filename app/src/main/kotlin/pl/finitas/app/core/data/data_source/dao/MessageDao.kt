package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.RoomMessage
import java.util.UUID

@Dao
interface MessageDao {

    @Query("SELECT * FROM RoomMessage WHERE idRoom = :idRoom ORDER BY createdAt DESC")
    fun getMessagesByIdRoomFlow(idRoom: UUID): Flow<List<RoomMessage>>

    @Query("SELECT * FROM RoomMessage GROUP BY idRoom HAVING max(createdAt)")
    fun getLastMessages(): Flow<List<RoomMessage>>

    @Query(
        """
        SELECT idRoom, count() as 'unreadCount'
        FROM RoomMessage
        WHERE idRoom in (:idsRoom) and isRead = 0
        GROUP BY idRoom
    """
    )
    suspend fun getUnreadMessageCountBy(idsRoom: List<UUID>): List<UnreadMessageCountByRoom>

    @Query("SELECT * FROM RoomMessage WHERE idRoom = :idRoom ORDER BY createdAt DESC")
    suspend fun getMessagesByIdRoom(idRoom: UUID): List<RoomMessage>

    @Upsert
    suspend fun upsertMessages(roomMessages: List<RoomMessage>)

    @Query("SELECT * FROM RoomMessage WHERE isPending = 1")
    suspend fun getPendingMessages(): List<RoomMessage>

    @Query("UPDATE RoomMessage SET isRead = 1 WHERE idMessage in (:idsMessage)")
    suspend fun setReadMessages(idsMessage: List<UUID>)
}

data class UnreadMessageCountByRoom(
    val idRoom: UUID,
    val unreadCount: Int,
)