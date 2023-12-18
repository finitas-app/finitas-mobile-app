package pl.finitas.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ProfileRepository {

    fun getAuthToken(): Flow<String?>

    suspend fun setAuthToken(authKey: String)

    fun getAuthorizedUserId(): Flow<UUID?>

    suspend fun setAuthorizedUserId(idUser: UUID)

    suspend fun clear()
}