package pl.finitas.app.profile_feature.domain

import pl.finitas.app.core.domain.ProfileRepository

class AuthService(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) {

    suspend fun signUp(signUpCommand: SignUpCommand) {
        authRepository.signUp(signUpCommand.toCreateUserRequest())
    }

    suspend fun signIn(signInCommand: SignInCommand) {
        val authResponse = authRepository.signIn(signInCommand.toAuthUserRequest())
        profileRepository.setAuthToken(authResponse.accessToken)
    }
}