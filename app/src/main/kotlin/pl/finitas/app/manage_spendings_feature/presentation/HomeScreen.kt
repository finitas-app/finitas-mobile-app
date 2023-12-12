package pl.finitas.app.manage_spendings_feature.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingDialog
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingsPanel

@Composable
fun HomeScreen(
    navController: NavController,
) {
    val totalSpendingViewModel: TotalSpendingViewModel = koinViewModel()
    val addSpendingViewModel: AddSpendingViewModel = koinViewModel()
    PrimaryBackground(isDialogOpen = addSpendingViewModel.isDialogOpen) {
        TotalSpendingsPanel(
            viewModel = totalSpendingViewModel,
            onAddSpendingClick = addSpendingViewModel::openDialog,
        )
        NavBar(navController = navController, backgroundColor = Colors.backgroundDark)
    }
    AddSpendingDialog(addSpendingViewModel)
}