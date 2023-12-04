package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Role
import pl.finitas.app.core.data.model.User

@Dao
interface RoleDao {

    @Insert
    suspend fun insertRole(role: Role): Long

    @Update
    suspend fun updateRole(role: Role)

    @Delete
    suspend fun deleteRole(role: Role)
}