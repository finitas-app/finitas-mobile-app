package pl.finitas.app.auth_feature.domain

interface AuthRepository {

    suspend fun signUp(createUserRequest: CreateUserRequest): CreateUserResponse

    suspend fun signIn(authUserRequest: AuthUserRequest): AuthUserResponse
}