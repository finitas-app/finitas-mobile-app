package pl.finitas.app.auth_feature.domain

import kotlinx.serialization.Serializable
import pl.finitas.app.auth_feature.presentation.CredentialsState
import pl.finitas.app.core.domain.dto.SerializableUUID

@Serializable
data class AuthUserRequest(val email: String, val password: String)

@Serializable
data class AuthUserResponse(val accessToken: String, val expires: Int, val idUser: SerializableUUID)

@Serializable
data class CreateUserRequest(val email: String, val password: String)

@Serializable
data class CreateUserResponse(val userId: SerializableUUID)

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


