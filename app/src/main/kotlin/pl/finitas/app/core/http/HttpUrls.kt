package pl.finitas.app.core.http

private const val frontendApiUrl = "http://192.168.1.45:8080/api"

object HttpUrls {
    const val signUp = "$frontendApiUrl/auth/signup"
    const val signIn = "$frontendApiUrl/auth/login"
    const val finishedSpendingsStore = "$frontendApiUrl/store/finished-spendings"
    const val shoppingListsStore = "$frontendApiUrl/store/shopping-lists"
    const val usersStore = "$frontendApiUrl/store/users"
}