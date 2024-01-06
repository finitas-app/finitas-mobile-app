package pl.finitas.app.core.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import org.koin.core.module.Module
import pl.finitas.app.core.domain.repository.ProfileRepository

private val urlsWithoutAuth = listOf<String>()

fun Module.httpClient() {
    single {
        val profileRepository: ProfileRepository = get()
        HttpClient(OkHttp) {
            install(Auth) {
                bearer {
                    loadTokens {
                        val authToken = profileRepository.getAuthToken().first()
                        BearerTokens(authToken ?: "", authToken ?: "")
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
            install(WebSockets)
            install(Logging) {
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json()
            }
            defaultRequest {
                contentType(contentType() ?: ContentType.Application.Json)
            }
            HttpResponseValidator {
                validateResponse { response ->
                    val httpStatusCode = response.status
                    if (httpStatusCode.value in 400..599) {
                        val error = response.body<ErrorResponse>()
                        if (error.errorCode == ErrorCode.AUTH_ERROR) {
                            profileRepository.clear()
                        }
                        throw FrontendApiException(
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

@Serializable
private data class ErrorResponse(
    val errorCode: ErrorCode,
    val errorMessage: String? = null,
)

class FrontendApiException(
    val statusCode: HttpStatusCode,
    val errorCode: ErrorCode,
    val errorMessage: String? = null,
) : Exception("{ statusCode: $statusCode, errorCode: $errorCode, errorMessage: $errorMessage }")

enum class ErrorCode {
    GENERIC_ERROR,
    AUTH_ERROR,
    ACTION_FORBIDDEN_ERROR,
    CONFIGURATION_ERROR,
    FILE_NOT_PROVIDED,
    INVALID_FILE_PROVIDED,
    ID_ROOM_NOT_PROVIDED,
    ID_USER_NOT_PROVIDED,
    FINISHED_SPENDING_NOT_FOUND,
    FINISHED_SPENDING_EXISTS,
    SHOPPING_LIST_NOT_FOUND,
    SHOPPING_LIST_EXISTS,
    USER_NOT_FOUND,
    STORE_REQUEST_INPUT_INVALID,
    SIGN_UP_LOGIN_INVALID,
    SIGN_UP_PASSWORD_WEAK,
    SIGN_UP_USER_EXISTS,
    ID_USER_INVALID,
    INVALID_UUID,
    CREATE_USER_ERROR,
    DELETE_USER_ERROR,
    STORE_REQUEST_INPUT_VALIDATION_FAILED,
    STORE_REQUEST_NON_PARSABLE,
    VISIBLE_NAME_INVALID,
    WRONG_AUTHORITY,
    USER_ALREADY_JOINED_ROOM,
    TRIED_SYNC_BY_MORE_THAN_ONE_USER,
}