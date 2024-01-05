package pl.finitas.app.sync_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.User
import java.util.UUID

interface UserRepository {

    suspend fun getUsers(): List<User>

    fun getUsernamesByIds(ids: List<UUID>): Flow<List<UsernameDto>>

    suspend fun getUserById(idUser: UUID): User?

    suspend fun addUserIfNotPresent(idUser: UUID)

    suspend fun getUserIds(): List<UUID>

    suspend fun saveUsers(users: List<User>)
}

data class UsernameDto(
    val idUser: UUID,
    val username: String,
)

