package pl.finitas.app.profile_feature.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pl.finitas.app.core.http.HttpUrls
import pl.finitas.app.profile_feature.domain.AuthRepository
import pl.finitas.app.profile_feature.domain.AuthUserRequest
import pl.finitas.app.profile_feature.domain.AuthUserResponse
import pl.finitas.app.profile_feature.domain.CreateUserRequest
import pl.finitas.app.profile_feature.domain.CreateUserResponse

class AuthRepositoryImpl(private val httpClient: HttpClient) : AuthRepository {
    override suspend fun signUp(createUserRequest: CreateUserRequest): CreateUserResponse{
        val response = httpClient.post {
            url(HttpUrls.signUp)
            contentType(ContentType.Application.Json)
            setBody(createUserRequest)
        }

        return response.body()
    }

    override suspend fun signIn(authUserRequest: AuthUserRequest): AuthUserResponse {
        val response = httpClient.post {
            url(HttpUrls.signIn)
            contentType(ContentType.Application.Json)
            setBody(authUserRequest)
        }

        return response.body()
    }
}