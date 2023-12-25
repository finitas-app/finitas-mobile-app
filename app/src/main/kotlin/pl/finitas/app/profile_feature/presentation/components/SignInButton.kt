package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import pl.finitas.app.auth_feature.presentation.components.AuthButton
import pl.finitas.app.navigation.NavPaths

@Composable
fun SignInButton(
    navController: NavController,
    modifier: Modifier,
) {
    AuthButton(
        text = "Sign in",
        onClick = {
            navController.navigate(NavPaths.AuthScreen.route)
        },
        modifier = Modifier.fillMaxWidth().then(modifier)
    )
}