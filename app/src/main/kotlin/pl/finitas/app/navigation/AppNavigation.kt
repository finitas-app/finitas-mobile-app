package pl.finitas.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.finitas.app.manage_additional_elements_feature.presentation.AdditionalElementsScreen
import pl.finitas.app.manage_spendings_feature.presentation.HomeScreen
import pl.finitas.app.profile_feature.presentation.AuthScreen
import pl.finitas.app.shopping_lists_feature.presentation.ShoppingListsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavPaths.HomeScreen.route) {
        composable(
            NavPaths.AuthScreen.route,
        ) {
            AuthScreen()
        }
        composable(
            NavPaths.HomeScreen.route,
        ) {
            HomeScreen(navController)
        }
        composable(
            NavPaths.AdditionalElementsScreen.route
        ) {
            AdditionalElementsScreen(navController)
        }
        composable(
            NavPaths.ShoppingLists.route
        ) {
            ShoppingListsScreen(navController)
        }
    }
}