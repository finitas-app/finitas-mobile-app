package pl.finitas.app.core.data.data_source.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.domain.repository.ProfileRepository
import java.util.UUID

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile")

class ProfileRepositoryImpl(
    private val context: Context
): ProfileRepository {

    private val authTokenKey = stringPreferencesKey("auth_token")
    private val idUserKey = stringPreferencesKey("id_user")

    override fun getAuthToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[authTokenKey]
        }
    }

    override suspend fun setAuthToken(authKey: String) {
        context.dataStore.edit { preferences ->
            preferences[authTokenKey] = authKey
        }
    }

    override fun getAuthorizedUserId(): Flow<UUID?> {
        return context.dataStore.data.map { preferences ->
            preferences[idUserKey]?.let { UUID.fromString(it) }
        }
    }

    override suspend fun setAuthorizedUserId(idUser: UUID) {
        context.dataStore.edit { preferences ->
            preferences[idUserKey] = idUser.toString()
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}