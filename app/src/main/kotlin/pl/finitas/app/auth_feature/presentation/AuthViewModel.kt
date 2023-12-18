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

class AuthViewModel(
    private val authService: AuthService,
) : ViewModel() {
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
        }
    }

    fun signIn(onSuccessfulLogin: () -> Unit) {
        viewModelScope.launch {
            authService.signIn(SignInCommand.from(credentialsState))
            onSuccessfulLogin()
        }
    }

    fun signUp() {
        viewModelScope.launch {
            authService.signUp(SignUpCommand.from(credentialsState))
        }
    }
}