package pl.finitas.app.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.domain.repository.ProfileRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile")


class ProfileRepositoryImpl(private val context: Context): ProfileRepository {

    private val authTokenKey = stringPreferencesKey("auth_token")

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

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}