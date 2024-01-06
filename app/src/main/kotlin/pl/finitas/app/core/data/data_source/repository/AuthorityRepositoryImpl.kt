package pl.finitas.app.core.data.data_source.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.data_source.dao.RoomMemberDao
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.domain.repository.AuthorityRepository
import java.util.UUID

class AuthorityRepositoryImpl(
    private val roomMemberDao: RoomMemberDao,
) : AuthorityRepository {
    override fun getAuthorityOfUser(idUser: UUID, idRoom: UUID): Flow<Set<Authority>> {
        return roomMemberDao.getAuthorityBy(idUser, idRoom).map { it?.authorities ?: setOf() }
    }
}