package pl.finitas.app.core.data.data_source.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.finitas.app.core.data.data_source.dao.FinishedSpendingDao
import pl.finitas.app.core.data.data_source.dao.RoomDao
import pl.finitas.app.core.data.data_source.dao.ShoppingListDao
import pl.finitas.app.core.data.data_source.dao.UserDao
import pl.finitas.app.core.domain.repository.ProfileRepository
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile")

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileRepositoryImpl(
    private val context: Context,
    private val roomDao: RoomDao,
    private val userDao: UserDao,
    private val shoppingListDao: ShoppingListDao,
    private val finishedSpendingDao: FinishedSpendingDao,
    private val clearDatabase: () -> Unit,
) : ProfileRepository {

    private val authTokenKey = stringPreferencesKey("auth_token")
    private val idUserKey = stringPreferencesKey("id_user")
    private val lastIdUserKey = stringPreferencesKey("last_id_user")

    override fun getAuthToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[authTokenKey]
        }
    }

    override suspend fun setAuthToken(authKey: String, idUser: UUID) {
        val lastIdUser = context.dataStore.data.map { it[lastIdUserKey] }.first()
        val idUserString = idUser.toString()
        context.dataStore.edit { preferences ->
            withContext(Dispatchers.IO) {
                if (lastIdUser != null && lastIdUser != idUserString) {
                    clearDatabase()
                }
                preferences[authTokenKey] = authKey
                preferences[idUserKey] = idUserString
            }
        }
    }

    override fun getUsername(): Flow<String?> {
        return getAuthorizedUserId().flatMapLatest { idUser ->
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
        val lastIdUser = context.dataStore.data.map { it[idUserKey] }.first()
        if (lastIdUser != null) {
            context.dataStore.edit { preferences ->
                preferences.clear()
                preferences[lastIdUserKey] = lastIdUser
                roomDao.deleteAllRooms()
                finishedSpendingDao.deleteAllForeignFinishedSpendings()
                shoppingListDao.deleteAllForeignShoppingLists()
            }
        }
    }
}