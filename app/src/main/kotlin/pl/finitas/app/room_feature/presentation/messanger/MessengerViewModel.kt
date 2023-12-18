package pl.finitas.app.room_feature.presentation.messanger

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.finitas.app.room_feature.domain.ChatMessage
import pl.finitas.app.room_feature.domain.IncomingTextMessage
import pl.finitas.app.room_feature.domain.service.MessageService
import pl.finitas.app.room_feature.domain.service.RoomService
import java.util.UUID

class MessengerViewModel(
    private val messageService: MessageService,
    private val roomService: RoomService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var roomTitle by mutableStateOf("")
        private set

    var messages: Flow<List<ChatMessage>> = emptyFlow()
        private set

    private var _idRoom by mutableStateOf<UUID?>(null)

    init {
        savedStateHandle.get<String>("idRoom")?.let { idRoomRaw ->
            val idRoom = UUID.fromString(idRoomRaw)
            _idRoom = idRoom


            viewModelScope.launch {
                // TODO: DELETE nullable
                roomTitle = roomService.getRoomById(idRoom!!)?.title ?: "Something"
            }
            messages = messageService.getMessagesByIdRoom(idRoom!!).map { messages ->
                val result = messages.mapIndexed { index, message ->
                    if (message !is IncomingTextMessage || index == messages.lastIndex) {
                        return@mapIndexed message
                    }

                    val nextMessage =
                        messages[index + 1] as? IncomingTextMessage ?: return@mapIndexed message

                    if ((nextMessage.sender ?: message.sender) == message.sender) {
                        return@mapIndexed message.copy(sender = null)
                    }

                    message
                }
                messageService.readMessage(
                    messages
                        .filter { (it as? IncomingTextMessage)?.isRead == false }
                        .map { it.idMessage }
                )
                result
            }
        } ?: throw IdRoomNotProvidedException()
    }

    fun sendTextMessage(message: String) {
        viewModelScope.launch {
            messageService.sendMessage(_idRoom ?: throw IdRoomNotProvidedException(), message)
        }
    }
}

class IdRoomNotProvidedException() : Exception("Id room not provided for messages screen.")
