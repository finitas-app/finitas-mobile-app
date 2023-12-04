package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.RoomMember

@Dao
interface RoomMemberDao {

    @Insert
    suspend fun insertRoomMember(roomMember: RoomMember): Long

    @Update
    suspend fun updateRoomMember(roomMember: RoomMember)

    @Delete
    suspend fun deleteRoomMember(roomMember: RoomMember)
}