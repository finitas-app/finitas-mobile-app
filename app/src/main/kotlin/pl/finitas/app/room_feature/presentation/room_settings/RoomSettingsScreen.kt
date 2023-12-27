package pl.finitas.app.room_feature.presentation.room_settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.room_feature.presentation.room_settings.components.RoomSettingsPanel

@Composable
fun RoomSettingsScreen(
    navController: NavController,
) {
    val viewModel: RoomSettingsViewModel = koinViewModel()


    SecondaryBackground {
        RoomSettingsPanel(navController, viewModel)
    }
}
