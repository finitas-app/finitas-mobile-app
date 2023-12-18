package pl.finitas.app.auth_feature.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.auth_feature.presentation.components.SignInPanel
import pl.finitas.app.auth_feature.presentation.components.SignUpPanel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.navigation.NavPaths

@Composable
fun AuthScreen(navController: NavController) {
    val viewModel: AuthViewModel = koinViewModel()

    PrimaryBackground (Modifier.blur(10.dp)){}


    Box(Modifier.fillMaxSize()) {
        when(viewModel.authType) {
            AuthType.SignIn -> SignInPanel(viewModel = viewModel, onSuccessfulLogin = {
                navController.navigate(NavPaths.RoomsScreen.route)
            })
            AuthType.SignUp -> SignUpPanel(viewModel = viewModel)
        }
    }
}