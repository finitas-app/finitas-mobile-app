package pl.finitas.app.auth_feature.domain

import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.sync_feature.domain.repository.UserRepository

class AuthService(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {

    suspend fun signUp(signUpCommand: SignUpCommand) {
        authRepository.signUp(signUpCommand.toCreateUserRequest())
    }

    suspend fun signIn(signInCommand: SignInCommand) {
        val authResponse = authRepository.signIn(signInCommand.toAuthUserRequest())
        profileRepository.setAuthToken(authResponse.accessToken)
        profileRepository.setAuthorizedUserId(authResponse.idUser)
        userRepository.addUserIfNotPresent(authResponse.idUser)
    }
}