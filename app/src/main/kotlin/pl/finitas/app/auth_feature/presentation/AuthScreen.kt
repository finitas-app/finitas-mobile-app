package pl.finitas.app.auth_feature.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.auth_feature.presentation.components.SignInPanel
import pl.finitas.app.auth_feature.presentation.components.SignUpPanel
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.navigation.NavPaths

@Composable
fun AuthScreen(navController: NavController) {
    val viewModel: AuthViewModel = koinViewModel()

    PrimaryBackground (Modifier.blur(10.dp)){}


    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = viewModel.authType == AuthType.SignIn,
            enter = fadeIn() + slideInHorizontally { -it },
            exit = slideOutHorizontally { -it } + fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            SignInPanel(viewModel = viewModel, onSuccessfulLogin = {
                navController.popBackStack()
            })
        }
        AnimatedVisibility(
            visible = viewModel.authType == AuthType.SignUp,
            enter = fadeIn() + slideInHorizontally { it },
            exit = slideOutHorizontally { it } + fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            SignUpPanel(viewModel = viewModel)
        }

        ClickableIcon(
            imageVector = Icons.Rounded.ArrowBackIos,
            onClick = { navController.navigate(NavPaths.ProfileScreen.route) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
    }
}