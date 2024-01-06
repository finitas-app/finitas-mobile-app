package pl.finitas.app.core.domain.services

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.domain.repository.AuthorityRepository
import pl.finitas.app.core.domain.repository.ProfileRepository
import java.util.UUID

class AuthorizedUserService(
    private val profileRepository: ProfileRepository,
    private val authorityRepository: AuthorityRepository,
) {
    fun getAuthorizedIdUser() = profileRepository.getAuthorizedUserId()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAuthorityOfCurrentUser(idRoom: UUID): Flow<Set<Authority>> {
        return getAuthorizedIdUser().flatMapLatest { idUser ->
            if (idUser == null) flow { emit(setOf()) }
            else authorityRepository.getAuthorityOfUser(idUser, idRoom)
        }
    }
}