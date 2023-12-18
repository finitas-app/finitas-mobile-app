package pl.finitas.app.core.presentation.components.rooms.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.core.presentation.components.background.secondaryBackgroundColor
import pl.finitas.app.room_feature.domain.RoomPreviewDto
import pl.finitas.app.room_feature.presentation.rooms.components.RoomCard
import java.util.UUID

@Composable
@Preview
fun RoomsTest() {
    SecondaryBackground {
        Box {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                repeat(20) {
                    item {
                        RoomCard(
                            RoomPreviewDto(
                                title = "First room",
                                lastMessage = "Last message in chat about something",
                                unreadMessagesNumber = 1,
                                idRoom = UUID.randomUUID(),
                            ),
                            {},
                            modifier = Modifier.clickable { })
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, secondaryBackgroundColor),
                            endY = with(LocalDensity.current) { 90.dp.toPx() }
                        )
                    )) {
            }
        }
    }
}