package pl.finitas.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.finitas.app.auth_feature.presentation.AuthScreen
import pl.finitas.app.manage_additional_elements_feature.presentation.AdditionalElementsScreen
import pl.finitas.app.manage_spendings_feature.presentation.HomeScreen
import pl.finitas.app.profile_feature.presentation.ProfileScreen
import pl.finitas.app.room_feature.presentation.messanger.MessengerScreen
import pl.finitas.app.room_feature.presentation.rooms.RoomsScreen
import pl.finitas.app.shopping_lists_feature.presentation.ShoppingListsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavPaths.HomeScreen.route
    ) {
        composable(
            NavPaths.ProfileScreen.route
        ) {
            ProfileScreen(navController)
        }
        composable(
            NavPaths.AuthScreen.route,
        ) {
            AuthScreen(navController)
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
        composable(
            NavPaths.RoomsScreen.route
        ) {
            RoomsScreen(navController)
        }
        composable(
            route = NavPaths.MessengerScreen.route + "?idRoom={idRoom}",
            arguments = listOf(
                navArgument(name = "idRoom") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            MessengerScreen(navController)
        }
    }
}