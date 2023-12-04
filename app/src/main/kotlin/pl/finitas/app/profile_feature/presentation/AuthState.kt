package pl.finitas.app.profile_feature.presentation

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