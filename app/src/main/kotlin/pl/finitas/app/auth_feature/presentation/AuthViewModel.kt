package pl.finitas.app.auth_feature.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.auth_feature.domain.AuthService
import pl.finitas.app.auth_feature.domain.SignInCommand
import pl.finitas.app.auth_feature.domain.SignUpCommand
import pl.finitas.app.core.http.ErrorCode
import pl.finitas.app.core.http.FrontendApiException

class AuthViewModel(
    private val authService: AuthService,
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var isDisableLogin by mutableStateOf(false)
        private set
    var isDisableSignUp by mutableStateOf(false)
        private set
    var errors by mutableStateOf<List<String>?>(null)
        private set
    var authType by mutableStateOf(AuthType.SignIn)
        private set

    var credentialsState by mutableStateOf(CredentialsState.empty)
        private set

    fun setLogin(login: String) {
        credentialsState = credentialsState.copy(login = login)
    }

    fun setPassword(password: String) {
        credentialsState = credentialsState.copy(password = password)
    }

    fun changeAuthType(authType: AuthType) {
        if (this.authType != authType) {
            this.authType = authType
            this.credentialsState = CredentialsState.empty
            this.errors = null
            when(authType) {
                AuthType.SignIn -> isDisableLogin = false
                AuthType.SignUp -> isDisableSignUp = false
            }
        }
    }

    fun signIn(onSuccessfulLogin: () -> Unit) {
        if (!isDisableLogin) {
            isDisableLogin = true
            isLoading = true
            viewModelScope.launch {
                try {
                    authService.signIn(SignInCommand.from(credentialsState))
                    onSuccessfulLogin()
                } catch (e: FrontendApiException) {
                    errors = listOf(
                        when (e.errorCode) {
                            ErrorCode.AUTH_ERROR -> {
                                "Incorrect e-mail or password"
                            }

                            else -> "There was an unspecified error"
                        }
                    )
                    isDisableLogin = false
                } catch (e: Exception) {
                    errors = listOf("Failed to login, check your internet connection")
                    e.printStackTrace()
                    isDisableLogin = false
                }
                isLoading = false
            }
        }
    }

    fun signUp() {
        if (!isLoading) {
            isLoading = true
            viewModelScope.launch {
                try {
                    authService.signUp(SignUpCommand.from(credentialsState))
                    changeAuthType(AuthType.SignIn)
                } catch (e: FrontendApiException) {
                    errors = listOf(
                        when (e.errorCode) {
                            ErrorCode.SIGN_UP_LOGIN_INVALID -> {
                                "The e-mail address is wrong"
                            }

                            ErrorCode.SIGN_UP_USER_EXISTS -> {
                                "A user with that e-mail address already exists"
                            }

                            ErrorCode.SIGN_UP_PASSWORD_WEAK -> {
                                "Password is too weak"
                            }

                            else -> "There was an unspecified error"
                        }
                    )
                } catch (e: Exception) {
                    errors = listOf("Failed to register, check your internet connection")
                }
                isLoading = false
            }
        }
    }
}