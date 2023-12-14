package pl.finitas.app.auth_feature.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.auth_feature.presentation.components.SignInPanel
import pl.finitas.app.auth_feature.presentation.components.SignUpPanel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground

@Composable
fun AuthScreen() {
    val viewModel: AuthViewModel = koinViewModel()

    PrimaryBackground (Modifier.blur(10.dp)){}


    Box(Modifier.fillMaxSize()) {
        when(viewModel.authType) {
            AuthType.SignIn -> SignInPanel(viewModel = viewModel)
            AuthType.SignUp -> SignUpPanel(viewModel = viewModel)
        }
    }
}