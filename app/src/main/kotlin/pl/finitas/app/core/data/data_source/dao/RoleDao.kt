package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.RoomRole

@Dao
interface RoleDao {

    @Insert
    suspend fun insertRole(roomRole: RoomRole): Long

    @Update
    suspend fun updateRole(roomRole: RoomRole)

    @Delete
    suspend fun deleteRole(roomRole: RoomRole)
}