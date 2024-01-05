package pl.finitas.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Authority
import java.util.UUID

interface AuthorityRepository {
    fun getAuthorityOfUser(idUser: UUID, idRoom: UUID): Flow<Set<Authority>>

}