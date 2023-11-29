package pl.finitas.app.core.presentation.components.rooms.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.core.presentation.components.rooms.RoomCard

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
                            "First room",
                            "Last message in chat about something",
                            1,
                            modifier = Modifier.clickable {  })
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
                NavBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .clickable(indication = null, interactionSource = MutableInteractionSource()) { println("Person") }
                )
            }
        }
    }
}