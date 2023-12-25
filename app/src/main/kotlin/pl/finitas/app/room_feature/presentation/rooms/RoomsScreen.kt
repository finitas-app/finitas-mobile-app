package pl.finitas.app.room_feature.presentation.rooms

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.room_feature.presentation.rooms.components.RoomsPanel

@Composable
fun RoomsScreen(navController: NavController) {
    val viewModel: RoomViewModel = koinViewModel()
    SecondaryBackground {
        RoomsPanel(
            roomViewModel = viewModel,
            navController = navController
        )
        NavBar(navController)
    }
}
