package pl.finitas.app.sync_feature.domain.repository

import pl.finitas.app.core.data.model.User
import java.util.UUID

interface UserRepository {

    suspend fun getUsers(): List<User>

    suspend fun getUsernamesByIds(ids: List<UUID>): List<UsernameDto>

    suspend fun addUserIfNotPresent(idUser: UUID)

    suspend fun getUserIds(): List<UUID>

    suspend fun saveUsers(users: List<User>)
}

data class UsernameDto(
    val idUser: UUID,
    val username: String,
)

