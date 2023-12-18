package pl.finitas.app.sync_feature.data.data_source

import pl.finitas.app.core.data.data_source.dao.UserDao
import pl.finitas.app.sync_feature.domain.repository.UsernameDto
import pl.finitas.app.sync_feature.domain.repository.UsersRepository
import java.util.UUID

class UsersRepositoryImpl(
    private val userDao: UserDao,
): UsersRepository {
    override suspend fun getUsernamesByIds(ids: List<UUID>): List<UsernameDto> {
        return userDao.getUsernamesByIds(ids)
    }

    override suspend fun readMessage(idsMessage: List<UUID>) {
        userDao.setReadMessages(idsMessage)
    }
}