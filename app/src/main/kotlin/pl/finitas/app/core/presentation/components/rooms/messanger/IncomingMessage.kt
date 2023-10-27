package pl.finitas.app.core.presentation.components.rooms.messanger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import pl.finitas.app.R
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import java.time.format.DateTimeFormatter

private val patternOfMessageTime = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun IncomingMessage(incomingChatMessage: IncomingChatMessage, modifier: Modifier = Modifier) {
    val (message, time, sender) = incomingChatMessage

    Box(
        modifier = modifier
            .padding(end = 30.dp)
            .background(
                color = colorResource(id = R.color.incoming_message_background),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 6.dp,
                ),
            )
            .padding(start = 13.dp, top = 13.dp, end = 9.dp, bottom = 9.dp)
    ) {
        Column {
            sender?.let {
                Fonts.chatMessagesHeader.Text(
                    text = sender,
                    color = Color(0xFFC7F83C),
                )
                Spacer(modifier = Modifier.height(7.dp))
            }

            Fonts.chatMessages.Text(
                text = message,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Fonts.chatMessageTime.Text(
                text = time.format(patternOfMessageTime),
                modifier = Modifier.align(Alignment.End),
                color = colorResource(id = R.color.background_light),
            )
        }
    }
}
