package pl.finitas.app.auth_feature.domain

import pl.finitas.app.auth_feature.presentation.CredentialsState
import java.util.UUID

data class AuthUserRequest(val email: String, val password: String)

data class AuthUserResponse(val accessToken: String, val expires: Int)

data class CreateUserRequest(val email: String, val password: String)

data class CreateUserResponse(val userId: UUID, val nickname: String)

data class SignUpCommand(
    val email: String,
    val password: String,
) {
    fun toCreateUserRequest() = CreateUserRequest(email, password)

    companion object {
        fun from(credentialsState: CredentialsState) = SignUpCommand(
            credentialsState.login,
            credentialsState.password,
        )
    }
}

data class SignInCommand(
    val email: String,
    val password: String,
) {
    fun toAuthUserRequest() = AuthUserRequest(email, password)

    companion object {
        fun from(credentialsState: CredentialsState) = SignInCommand(
            credentialsState.login,
            credentialsState.password,
        )
    }
}


