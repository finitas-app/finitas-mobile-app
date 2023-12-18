package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.finitas.app.core.data.model.User
import pl.finitas.app.sync_feature.domain.repository.UsernameDto
import java.util.UUID

@Dao
interface UserDao {

    @Query("SELECT idUser, username FROM User WHERE idUser in (:ids)")
    suspend fun getUsernamesByIds(ids: List<UUID>): List<UsernameDto>

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE RoomMessage SET isRead = 1 WHERE idMessage in (:idsMessage)")
    suspend fun setReadMessages(idsMessage: List<UUID>)
}