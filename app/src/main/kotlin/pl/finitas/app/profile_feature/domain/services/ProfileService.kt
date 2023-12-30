package pl.finitas.app.profile_feature.domain.services

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.domain.dto.store.VisibleName
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.UserStoreRepository

class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userStoreRepository: UserStoreRepository,
) {

    fun getToken() = profileRepository.getAuthToken()
    suspend fun logout() = profileRepository.clear()
    suspend fun setUsername(username: String) {
        try {
            userStoreRepository.patchVisibleName(visibleName = VisibleName(username))
            profileRepository.setUsername(username)
        } catch (_: Exception) { }
    }

    fun getUsername(): Flow<String?> = profileRepository.getUsername()
}