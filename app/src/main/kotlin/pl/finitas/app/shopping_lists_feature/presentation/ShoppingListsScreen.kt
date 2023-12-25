package pl.finitas.app.shopping_lists_feature.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list.ShoppingListPanel
import pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list.ShoppingListViewModel
import pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list.UpsertShoppingList
import pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list.UpsertShoppingListViewModel

@Composable
fun ShoppingListsScreen(
    navController: NavController,
) {
    val upsertShoppingListViewModel: UpsertShoppingListViewModel = koinViewModel()
    val shoppingListViewModel: ShoppingListViewModel = koinViewModel()
    PrimaryBackground(isDialogOpen = upsertShoppingListViewModel.isDialogOpen) {
        ShoppingListPanel(
            viewModel = shoppingListViewModel,
            onUpsertShoppingListClick = { upsertShoppingListViewModel.openDialog(it) }
        )
        NavBar(navController = navController)
    }
    UpsertShoppingList(upsertShoppingListViewModel)
}