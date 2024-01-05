package pl.finitas.app.sync_feature.data.data_source

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.UserDao
import pl.finitas.app.core.data.model.User
import pl.finitas.app.sync_feature.domain.repository.UserRepository
import pl.finitas.app.sync_feature.domain.repository.UsernameDto
import java.util.UUID

class UserRepositoryImpl(
    private val userDao: UserDao,
): UserRepository {
    override suspend fun getUsers(): List<User> {
        return userDao.getAllUsers()
    }

    override fun getUsernamesByIds(ids: List<UUID>): Flow<List<UsernameDto>> {
        return userDao.getUsernamesByIds(ids)
    }

    override suspend fun getUserById(idUser: UUID): User? {
        return userDao.getUserById(idUser)
    }

    override suspend fun addUserIfNotPresent(idUser: UUID) {
        userDao.addUserIfNotPresent(idUser)
    }

    override suspend fun getUserIds(): List<UUID> {
        return userDao.getAllUsers().map { it.idUser }
    }

    override suspend fun saveUsers(users: List<User>) {
        return userDao.saveUsers(users)
    }
}