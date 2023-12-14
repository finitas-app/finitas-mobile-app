package pl.finitas.app.core.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import kotlinx.coroutines.flow.first
import org.koin.core.module.Module
import pl.finitas.app.core.domain.repository.ProfileRepository

private val urlsWithoutAuth = listOf<String>()

fun Module.httpClient() {
    single {
        val profileRepository: ProfileRepository = get()
        HttpClient(Android) {
            install(Auth) {
                bearer {
                    loadTokens {
                        val authToken = profileRepository.getAuthToken().first()
                        BearerTokens( authToken ?: "", authToken ?: "")
                    }
                    refreshTokens {
                        profileRepository.clear()
                        BearerTokens("", "")
                    }
                    sendWithoutRequest { request ->
                        request.url.toString() in urlsWithoutAuth
                    }
                }
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                jackson()
            }
            HttpResponseValidator {
                validateResponse { response ->
                    val httpStatusCode = response.status
                    if (httpStatusCode.value in 400..599) {
                        val error = response.body<ErrorResponse>()
                        if (error.errorCode == ErrorCode.AUTH_ERROR) {
                            profileRepository.clear()
                        }
                        else throw FrontendApiException(
                            statusCode = httpStatusCode,
                            errorCode = error.errorCode,
                            errorMessage = error.errorMessage
                        )
                    }
                }
            }
        }
    }
}

private data class ErrorResponse(
    val errorCode: ErrorCode,
    val errorMessage: String? = null,
)

class FrontendApiException(
    val statusCode: HttpStatusCode,
    val errorCode: ErrorCode,
    val errorMessage: String? = null,
) : Exception()

enum class ErrorCode {
    GENERIC_ERROR,
    AUTH_ERROR,
    CONFIGURATION_ERROR,
    FILE_NOT_PROVIDED,
    INVALID_FILE_PROVIDED,
}