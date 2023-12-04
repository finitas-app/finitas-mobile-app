package pl.finitas.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.finitas.app.manage_spendings_feature.presentation.HomeScreen
import pl.finitas.app.profile_feature.presentation.AuthScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavPaths.AuthScreen.route) {
        composable(
            NavPaths.HomeScreen.route,
        ) {
            HomeScreen()
        }
        composable(
            NavPaths.AuthScreen.route,
        ) {
            AuthScreen()
        }
    }
}