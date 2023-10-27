package pl.finitas.app.core.presentation.components.rooms.messanger

import java.time.LocalTime

sealed interface ChatMessage {
    val message: String
    val time: LocalTime
}

data class IncomingChatMessage(override val message: String, override val time: LocalTime, val sender: String?): ChatMessage
data class OutgoingChatMessage(override val message: String, override val time: LocalTime): ChatMessage