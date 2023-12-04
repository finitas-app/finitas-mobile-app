package pl.finitas.app.core.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getAuthToken(): Flow<String?>

    suspend fun setAuthToken(authKey: String)

    suspend fun clear()
}