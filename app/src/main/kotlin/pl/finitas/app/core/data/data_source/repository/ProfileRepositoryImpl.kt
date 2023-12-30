package pl.finitas.app.core.data.data_source.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.data_source.dao.RoomDao
import pl.finitas.app.core.data.data_source.dao.UserDao
import pl.finitas.app.core.domain.repository.ProfileRepository
import java.util.UUID

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile")

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileRepositoryImpl(
    private val context: Context,
    private val roomDao: RoomDao,
    private val userDao: UserDao,
): ProfileRepository {

    private val authTokenKey = stringPreferencesKey("auth_token")
    private val idUserKey = stringPreferencesKey("id_user")

    override fun getAuthToken(): Flow<String?> {
        context.dataStore.data.map {  }
        return context.dataStore.data.map { preferences ->
            preferences[authTokenKey]
        }
    }

    override suspend fun setAuthToken(authKey: String, idUser: UUID) {
        context.dataStore.edit { preferences ->
            preferences[authTokenKey] = authKey
            preferences[idUserKey] = idUser.toString()
        }
    }

    override fun getUsername(): Flow<String?> {
        return getAuthorizedUserId().flatMapMerge { idUser ->
            if (idUser == null) flow {}
            else userDao.getUserByIdFlow(idUser).map { it?.username }
        }
    }

    override fun getAuthorizedUserId(): Flow<UUID?> {
        return context.dataStore.data.map { preferences ->
            preferences[idUserKey]?.let { UUID.fromString(it) }
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
            roomDao.deleteAllRooms()
        }
    }
}