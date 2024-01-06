package pl.finitas.app.profile_feature.domain.services

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.domain.dto.store.VisibleName
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.core.domain.validateBuilder

class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userStoreRepository: UserStoreRepository,
) {

    fun getToken() = profileRepository.getAuthToken()
    suspend fun logout() = profileRepository.clear()
    suspend fun setUsername(username: String) {
        validateBuilder {
            validate(username.isNotBlank()) { "Your name can't be blank" }
        }
        userStoreRepository.patchVisibleName(visibleName = VisibleName(username.trim()))
    }

    fun getUsername(): Flow<String?> = profileRepository.getUsername()
}