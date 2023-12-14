package pl.finitas.app.core.http

private const val frontendApiUrl = "http://192.168.1.45:8080/api"

object HttpUrls {
    const val signUp = "$frontendApiUrl/auth/signup"
    const val signIn = "$frontendApiUrl/auth/login"
}