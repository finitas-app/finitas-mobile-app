package pl.finitas.app.auth_feature.presentation

data class CredentialsState(
    val login: String,
    val password: String,
) {
    companion object {
        val empty get() = CredentialsState("", "")
    }
}

enum class AuthType {
    SignIn,
    SignUp,
}