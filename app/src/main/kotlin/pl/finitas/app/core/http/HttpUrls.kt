package pl.finitas.app.core.http

private const val host = "0.0.0.0"
private const val port = 8080
const val frontendApiUrl = "http://$host:$port/api"

object HttpUrls {
    const val signUp = "$frontendApiUrl/auth/signup"
    const val signIn = "$frontendApiUrl/auth/login"
    const val finishedSpendingsStore = "$frontendApiUrl/store/finished-spendings"
    const val shoppingListsStore = "$frontendApiUrl/store/shopping-lists"
    const val usersStore = "$frontendApiUrl/store/users"
    const val synchronizationWebsocket = "ws://$host:$port/api/synchronizations"
    const val syncMessages = "$frontendApiUrl/rooms/messages/sync"
    const val syncRooms = "$frontendApiUrl/rooms/sync"
    const val sendMessage = "$frontendApiUrl/rooms/messages"
    const val addRoom = "$frontendApiUrl/rooms"
    const val parseReceipt = "$frontendApiUrl/receipts/parse"
    const val joinRoom = "$frontendApiUrl/rooms/users"
}