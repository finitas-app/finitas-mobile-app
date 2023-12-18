package pl.finitas.app.sync_feature.domain.repository

import java.util.UUID

interface UsersRepository {
    suspend fun getUsernamesByIds(ids: List<UUID>): List<UsernameDto>
    suspend fun readMessage(idsMessage: List<UUID>)
}

data class UsernameDto(
    val idUser: UUID,
    val username: String,
)