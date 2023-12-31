package pl.finitas.app.room_feature.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.core.domain.repository.MessageSenderRepository
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.SendMessageRequest
import pl.finitas.app.core.domain.repository.SingleMessageDto
import pl.finitas.app.room_feature.domain.ChatMessage
import pl.finitas.app.room_feature.domain.IncomingShoppingListMessage
import pl.finitas.app.room_feature.domain.IncomingTextMessage
import pl.finitas.app.room_feature.domain.OutgoingShoppingListMessage
import pl.finitas.app.room_feature.domain.OutgoingTextMessage
import pl.finitas.app.room_feature.domain.repository.MessageRepository
import pl.finitas.app.room_feature.domain.repository.RoomRepository
import pl.finitas.app.sync_feature.domain.repository.UserRepository
import java.time.LocalDateTime
import java.util.UUID

class MessageService(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val messageSenderRepository: MessageSenderRepository,
) {
    fun getMessagesByIdRoom(idRoom: UUID): Flow<List<ChatMessage>> = messageRepository.getMessagesByIdRoom(idRoom).map { messages ->
        val currentUserId = profileRepository.getAuthorizedUserId().first() ?: UUID.randomUUID()// todo: throw UserNotAuthorized()
        val userNames = userRepository.getUsernamesByIds(
            messages
                .map { it.idUser }
                .distinct()
        ).groupBy { it.idUser }.mapValues { it.value[0].username }
        messages.map { message ->
            if (message.idUser == currentUserId) {
                when {
                    message.content != null -> {
                        OutgoingTextMessage(
                            idMessage = message.idMessage,
                            message = message.content,
                            time = message.createdAt,
                            isPending = message.isPending
                        )
                    }
                    message.idShoppingList != null -> {
                        OutgoingShoppingListMessage(
                            idMessage = message.idMessage,
                            idShoppingList = message.idShoppingList,
                            time = message.createdAt,
                            isPending = message.isPending
                        )
                    }
                    else -> {
                        throw MessagesParseException(message)
                    }
                }
            } else {
                when {
                    message.content != null -> {
                        IncomingTextMessage(
                            idMessage = message.idMessage,
                            message = message.content,
                            time = message.createdAt,
                            sender = userNames[message.idUser] ?: throw UsernameNotFoundException(message.idUser),
                            isRead = message.isRead,
                        )
                    }
                    message.idShoppingList != null -> {
                        IncomingShoppingListMessage(
                            idMessage = message.idMessage,
                            idShoppingList = message.idShoppingList,
                            time = message.createdAt,
                            sender = userNames[message.idUser] ?: throw UsernameNotFoundException(message.idUser),
                            isRead = message.isRead,
                        )
                    }
                    else -> {
                        throw MessagesParseException(message)
                    }
                }
            }
        }
    }

    suspend fun readMessage(idsMessage: List<UUID>) {
        messageRepository.readMessage(idsMessage)
    }

    suspend fun sendMessage(idRoom: UUID, message: String) {
        val idMessage = UUID.randomUUID()
        val idUser = profileRepository.getAuthorizedUserId().first() ?: throw UnauthorizedUserException()
        messageRepository.saveMessage(
            RoomMessage(
                idMessage = idMessage,
                idUser = idUser,
                idRoom = idRoom,
                createdAt = LocalDateTime.now(),
                isPending = true,
                isRead = true,
                version = -1,
                idShoppingList = null,
                content = message,
            )
        )
        try {
            messageSenderRepository.sendMessages(
                SendMessageRequest(
                    listOf(
                        SingleMessageDto(
                            idMessage = idMessage,
                            idRoom = idRoom,
                            content = message,
                        )
                    )
                )
            )

        }catch (_: Exception) {

        }
    }

    suspend fun sendMessage(idRoom: UUID, idShoppingList: UUID) {
        val idMessage = UUID.randomUUID()
        val idUser = profileRepository.getAuthorizedUserId().first() ?: throw UnauthorizedUserException()
        messageRepository.saveMessage(
            RoomMessage(
                idMessage = idMessage,
                idUser = idUser,
                idRoom = idRoom,
                createdAt = LocalDateTime.now(),
                isPending = true,
                isRead = true,
                version = -1,
                idShoppingList = idShoppingList,
                content = null,
            )
        )
        try {
            messageSenderRepository.sendMessages(
                SendMessageRequest(
                    listOf(
                        SingleMessageDto(
                            idMessage = idMessage,
                            idRoom = idRoom,
                            idShoppingList = idShoppingList,
                        )
                    )
                )
            )

        }catch (_: Exception) {

        }
    }
}

class MessagesParseException(message: RoomMessage) : Exception("During parsing of messages a problem occurred: $message")
class UsernameNotFoundException(idUser: UUID): Exception("Username with id '$idUser' not found exception!")

class UnauthorizedUserException: Exception("UnauthorizedUser!")
