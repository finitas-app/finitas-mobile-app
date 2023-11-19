package pl.finitas.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.finitas.app.manage_spendings_feature.presentation.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavPaths.HomeScreen.route + "?homeId=23") {
        composable(
            NavPaths.HomeScreen.route,
        ) {
            HomeScreen()
        }
    }
}