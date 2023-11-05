package pl.finitas.app.core.presentation.components.rooms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun RoomCard(title: String, lastMessage: String, unreadMessagesNumber: Int, modifier: Modifier = Modifier) {
    val icon = Icons.Rounded.AccountCircle

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp, 12.dp, 15.dp, 0.dp)
            .then(modifier)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = "Room avatar", Modifier.size(64.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Fonts.regular.Text(text = title)
                Fonts.regular.Text(
                    text = "${lastMessage.take(24)}...",
                    color = Color.White.copy(alpha = .5f),
                )
            }
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Colors.backgroundLight,
                    shape = CircleShape,
                )
                .align(alignment = Alignment.CenterVertically)
        ) {
            Fonts.regular.Text(
                "$unreadMessagesNumber",
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}