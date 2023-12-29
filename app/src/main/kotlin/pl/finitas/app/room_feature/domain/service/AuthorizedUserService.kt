package pl.finitas.app.room_feature.domain.service

import pl.finitas.app.core.domain.repository.ProfileRepository

class AuthorizedUserService(
    private val profileRepository: ProfileRepository,
) {
    fun getAuthorizedIdUser() = profileRepository.getAuthorizedUserId()
}