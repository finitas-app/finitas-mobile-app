package pl.finitas.app.core.domain.repository

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID

interface MessageSenderRepository {
    suspend fun sendMessages(sendMessageRequest: SendMessageRequest)
}

@Serializable
data class SendMessageRequest(
    val messages: List<SingleMessageDto>,
)

@Serializable
data class SingleMessageDto(
    val idMessage: SerializableUUID,
    val idRoom: SerializableUUID,
    val idShoppingList: SerializableUUID? = null,
    val content: String? = null,
)