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
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import java.time.format.DateTimeFormatter

private val patternOfMessageTime = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun OutgoingMessage(outgoingChatMessage: OutgoingChatMessage, modifier: Modifier = Modifier) {
    val (message, time) = outgoingChatMessage

    Box(
        modifier = modifier
            .padding(start = 30.dp)
            .background(
                color = Colors.mirrorSpendingList,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 6.dp,
                    bottomStart = 16.dp,
                ),
            )
            .padding(start = 13.dp, top = 13.dp, end = 9.dp, bottom = 9.dp)
    ) {
        Column {
            Fonts.chatMessages.Text(
                text = message,
                color = Color.White,
                modifier = Modifier.padding(end = 4.dp),
            )
            Spacer(modifier = Modifier.height(3.dp))
            Fonts.chatMessageTime.Text(
                text = time.format(patternOfMessageTime),
                modifier = Modifier.align(Alignment.End),
                color = Colors.backgroundLight,
            )
        }
    }
}