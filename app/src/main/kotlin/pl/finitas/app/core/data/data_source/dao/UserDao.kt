package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
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

    @Query("SELECT * FROM User WHERE idUser = :idUser")
    suspend fun getUserById(idUser: UUID): User?


    @Query("SELECT * FROM User WHERE idUser = :idUser")
    fun getUserByIdFlow(idUser: UUID): Flow<User?>

    @Transaction
    suspend fun addUserIfNotPresent(idUser: UUID) {
        getUserById(idUser) ?: insertUser(User(username = "", idUser = idUser))
    }
    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>
    @Upsert
    suspend fun saveUsers(users: List<User>)

}