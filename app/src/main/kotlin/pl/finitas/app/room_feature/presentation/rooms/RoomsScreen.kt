package pl.finitas.app.room_feature.presentation.rooms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.domain.emptyUUID
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.navigation.NavPaths
import pl.finitas.app.room_feature.presentation.rooms.components.RoomsPanel

@Composable
fun RoomsScreen(navController: NavController) {
    val viewModel: RoomViewModel = koinViewModel()
    val idUser by viewModel.authorizedUserId.collectAsState(emptyUUID)
    if (idUser == emptyUUID) return
    if (idUser == null) {
        navController.navigate(NavPaths.AuthScreen.route, navOptions = NavOptions.Builder().build())
        return
    }
    SecondaryBackground {
        RoomsPanel(
            roomViewModel = viewModel,
            navController = navController
        )
        NavBar(navController)
    }
}
