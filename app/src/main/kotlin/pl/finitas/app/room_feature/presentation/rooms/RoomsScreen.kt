package pl.finitas.app.room_feature.presentation.rooms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.navigation.NavPaths
import pl.finitas.app.room_feature.presentation.rooms.components.RoomCard

@Composable
fun RoomsScreen(navController: NavController) {
    val viewModel: RoomViewModel = koinViewModel()
    val rooms by viewModel.roomsState.collectAsState(initial = listOf())

    SecondaryBackground {
        Column(Modifier.fillMaxSize()) {
            RoomsHeader(
                onAddClick = {},
                modifier = Modifier
                    .padding(top = 12.dp)
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                rooms.forEach {
                    RoomCard(roomPreviewDto = it, onClick = { idRoom ->
                        navController.navigate(
                            NavPaths.MessengerScreen.route + "?idRoom=$idRoom"
                        )
                    })
                }
            }
        }
        NavBar(navController)
    }
}

@Composable
private fun RoomsHeader(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Fonts.heading1.Text(
            text = "Rooms",
            modifier = Modifier.align(Alignment.Center)
        )
        ClickableIcon(
            imageVector = Icons.Rounded.Add,
            onClick = onAddClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
