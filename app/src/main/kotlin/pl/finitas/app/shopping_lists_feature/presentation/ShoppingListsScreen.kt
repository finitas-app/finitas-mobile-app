package pl.finitas.app.shopping_lists_feature.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar


@Composable
fun ShoppingListsScreen(
    navController: NavController,
) {
    PrimaryBackground {
        NavBar(navController = navController)
    }
}
