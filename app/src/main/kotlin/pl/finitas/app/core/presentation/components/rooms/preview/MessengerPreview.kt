package pl.finitas.app.core.presentation.components.rooms.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.core.presentation.components.rooms.messanger.ChatMessage
import pl.finitas.app.core.presentation.components.rooms.messanger.IncomingChatMessage
import pl.finitas.app.core.presentation.components.rooms.messanger.IncomingMessage
import pl.finitas.app.core.presentation.components.rooms.messanger.MessengerInput
import pl.finitas.app.core.presentation.components.rooms.messanger.OutgoingChatMessage
import pl.finitas.app.core.presentation.components.rooms.messanger.OutgoingMessage
import java.time.LocalTime

@Composable
@Preview
fun MessengerTest() {
    var messages by remember {
        mutableStateOf(
            listOf<ChatMessage>(
                IncomingChatMessage(
                    "Hello world message",
                    LocalTime.of(17, 3),
                    "Marharyta",
                ),
                IncomingChatMessage(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent malesuada maximus sem, eu finibus arcu rutrum a. Aliquam at nunc porttitor nunc sollicitudin venenatis quis ac massa.",
                    LocalTime.of(17, 3),
                    "Marharyta",
                ),
                OutgoingChatMessage(
                    "Good job",
                    LocalTime.of(17, 3),
                ),
                OutgoingChatMessage(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    LocalTime.of(17, 3),
                ),
            )
        )
    }

    val preparedMessages = messages.mapIndexed { index, message ->
        if (message !is IncomingChatMessage || index == 0) {
            return@mapIndexed message
        }

        val previousMessage =
            messages[index - 1] as? IncomingChatMessage ?: return@mapIndexed message

        if ((previousMessage.sender ?: message.sender) == message.sender) {
            return@mapIndexed message.copy(sender = null)
        }

        message
    }

    SecondaryBackground {
        Column(Modifier.padding(15.dp, 50.dp)) {
            preparedMessages.forEach { message ->
                when (message) {
                    is IncomingChatMessage -> IncomingMessage(message)
                    is OutgoingChatMessage -> OutgoingMessage(
                        outgoingChatMessage = message,
                        modifier = Modifier.align(Alignment.End),
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }

        MessengerInput(
            onSendMessage = {
                messages = messages + OutgoingChatMessage(it, LocalTime.now())
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
        )
    }
}