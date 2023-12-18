package pl.finitas.app.room_feature.presentation.messanger.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.room_feature.domain.OutgoingTextMessage
import java.time.format.DateTimeFormatter

private val patternOfMessageTime = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun OutgoingMessage(outgoingTextMessage: OutgoingTextMessage, modifier: Modifier = Modifier) {
    val (message, time) = outgoingTextMessage

    Box(modifier = Modifier.fillMaxWidth()) {
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
                .align(Alignment.CenterEnd)
        ) {
            Column {
                Fonts.chatMessages.Text(
                    text = message,
                    color = Color.White,
                    modifier = Modifier.padding(end = 4.dp),
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(modifier = Modifier
                    .height(
                        with(LocalDensity.current) {
                            14.sp.toDp()
                        }
                    )
                    .align(Alignment.End)) {
                    if (!outgoingTextMessage.isPending) {
                        Fonts.chatMessageTime.Text(
                            text = time.format(patternOfMessageTime),
                            color = Colors.backgroundLight,
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(10.dp),
                            trackColor = Colors.mirrorSpendingList,
                            color = Colors.backgroundLight,
                            strokeWidth = 1.dp
                        )
                    }
                }
            }
        }
    }
}
