package pl.finitas.app.profile_feature.domain.services

import pl.finitas.app.core.domain.repository.ProfileRepository

class ProfileService(
    private val profileRepository: ProfileRepository,
) {

    fun getToken() = profileRepository.getAuthToken()

    suspend fun logout() = profileRepository.clear()
}