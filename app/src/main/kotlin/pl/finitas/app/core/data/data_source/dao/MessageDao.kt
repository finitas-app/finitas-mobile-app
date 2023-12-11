package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.RoomMessage

@Dao
interface MessageDao {

    @Insert
    suspend fun insertMessage(roomMessage: RoomMessage): Long

    @Update
    suspend fun updateMessage(roomMessage: RoomMessage)

    @Delete
    suspend fun deleteMessage(roomMessage: RoomMessage)
}