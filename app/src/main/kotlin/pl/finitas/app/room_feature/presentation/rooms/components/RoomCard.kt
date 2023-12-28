package pl.finitas.app.room_feature.presentation.rooms.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.core.presentation.components.utils.trimOnOverflow
import pl.finitas.app.room_feature.domain.RoomPreviewDto
import java.util.UUID

@Composable
fun RoomCard(
    roomPreviewDto: RoomPreviewDto,
    onClick: (UUID) -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = Icons.Rounded.AccountCircle
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp, 12.dp, 15.dp, 0.dp)
            .then(modifier)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { onClick(roomPreviewDto.idRoom) },
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = "Room avatar", Modifier.size(64.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Fonts.regular.Text(text = roomPreviewDto.title)
                Fonts.regular.Text(
                    text =
                    if (roomPreviewDto.lastMessage.contains("\n")) {
                        roomPreviewDto
                            .lastMessage
                            .split("\n")
                            .let { "${it[0]}..." }
                    } else {
                        roomPreviewDto.lastMessage.trimOnOverflow()
                    },
                    color = Color.White.copy(alpha = .5f),
                )
            }
        }
        if (roomPreviewDto.unreadMessagesNumber != 0) {
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
                    "${roomPreviewDto.unreadMessagesNumber}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}