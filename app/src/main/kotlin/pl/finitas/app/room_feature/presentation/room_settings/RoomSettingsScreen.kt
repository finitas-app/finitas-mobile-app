package pl.finitas.app.room_feature.presentation.room_settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.domain.emptyUUID
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.navigation.NavPaths
import pl.finitas.app.room_feature.presentation.room_settings.components.RoomSettingsPanel

@Composable
fun RoomSettingsScreen(
    navController: NavController,
) {
    val viewModel: RoomSettingsViewModel = koinViewModel()
    val idUser by viewModel.authorizedUserId.collectAsState(emptyUUID)

    if (idUser == null) {
        navController.navigate(NavPaths.AuthScreen.route)
    }

    SecondaryBackground {
        RoomSettingsPanel(navController, viewModel)
    }
}
