package pl.finitas.app.room_feature.presentation.messanger

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.room_feature.domain.ChatMessage
import pl.finitas.app.room_feature.domain.IncomingChatMessage
import pl.finitas.app.room_feature.domain.ShoppingListMessage
import pl.finitas.app.room_feature.domain.service.AuthorizedUserService
import pl.finitas.app.room_feature.domain.service.MessageService
import pl.finitas.app.room_feature.domain.service.RoomService
import pl.finitas.app.room_feature.domain.service.RoomShoppingListService
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class MessengerViewModel(
    private val messageService: MessageService,
    private val roomService: RoomService,
    private val authorizedUserService: AuthorizedUserService,
    private val roomShoppingListService: RoomShoppingListService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var authorizedUserId = authorizedUserService.getAuthorizedIdUser()

    val roomTitle: Flow<String>

    var messages: Flow<List<ChatMessage>>
        private set

    val shoppingLists: Flow<Map<UUID, ShoppingList>>

    val shoppingListsPreview = roomShoppingListService.getShoppingListsPreview()

    var idRoom by mutableStateOf<UUID?>(UUID.randomUUID())
        private set

    var isSendObjectDialogOpen by mutableStateOf(false)
        private set

    init {
        val idRoom = savedStateHandle
            .get<String>("idRoom")
            ?.let { UUID.fromString(it) } ?: throw IdRoomNotProvidedException()
        this@MessengerViewModel.idRoom = idRoom
        viewModelScope.launch {
            roomService.getRoomsPreview().collect { rooms ->
                this@MessengerViewModel.idRoom = idRoom
                if (idRoom !in rooms.map { it.idRoom }) {
                    this@MessengerViewModel.idRoom = null
                }
            }
        }

        roomTitle = roomService.findRoomById(idRoom).map { it?.title ?: "" }

        messages = messageService.getMessagesByIdRoom(idRoom).map { messages ->
            val result = messages.mapIndexed { index, message ->
                if (message !is IncomingChatMessage || index == messages.lastIndex) {
                    return@mapIndexed message
                }

                val nextMessage =
                    messages[index + 1] as? IncomingChatMessage ?: return@mapIndexed message

                if ((nextMessage.sender ?: message.sender) == message.sender) {
                    return@mapIndexed message.clearSender()
                }

                message
            }
            messageService.readMessage(
                messages
                    .filter { (it as? IncomingChatMessage)?.isRead == false }
                    .map { it.idMessage }
            )
            result
        }
        shoppingLists = messages.flatMapLatest { messages ->
            roomShoppingListService.getShoppingListsBy(
                messages
                    .filterIsInstance<ShoppingListMessage>()
                    .map { it.idShoppingList }
            ).map { shoppingLists ->
                shoppingLists.associateBy { it.idShoppingList }
            }
        }
    }

    fun sendTextMessage(message: String) {
        viewModelScope.launch {
            messageService.sendMessage(idRoom ?: throw IdRoomNotProvidedException(), message)
        }
    }

    fun sendShoppingList(idShoppingList: UUID) {
        viewModelScope.launch {
            messageService.sendMessage(idRoom ?: throw IdRoomNotProvidedException(), idShoppingList)
            closeSendObjectDialog()
        }
    }

    fun openSendObjectDialog() {
        isSendObjectDialogOpen = true
    }

    fun closeSendObjectDialog() {
        isSendObjectDialogOpen = false
    }
}

class IdRoomNotProvidedException() : Exception("Id room not provided for messages screen.")
