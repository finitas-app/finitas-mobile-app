package pl.finitas.app.room_feature.presentation.rooms.components

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
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.navigation.NavPaths
import pl.finitas.app.room_feature.presentation.rooms.RoomViewModel

@Composable
fun RoomsPanel(
    roomViewModel: RoomViewModel,
    navController: NavController,
) {
    val rooms by roomViewModel.roomsState.collectAsState(initial = listOf())
    Column(Modifier.fillMaxSize()) {
        RoomsHeader(
            onAddClick = { roomViewModel.openAddDialog() },
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
    AddRoomDialog(
        isDialogOpen = roomViewModel.isAddRoomDialogOpen,
        onClose = roomViewModel::closeAddDialog,
        onSaveRoom = { roomViewModel.addRoom(it) },
    )
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
