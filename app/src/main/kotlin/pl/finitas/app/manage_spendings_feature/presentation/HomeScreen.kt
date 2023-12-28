package pl.finitas.app.manage_spendings_feature.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingDialog
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartDisplayViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.components.ChartConstructor
import pl.finitas.app.manage_spendings_feature.presentation.charts.components.ChartPanel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.FinishedSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.FinishedSpendingsPanel

@Composable
fun HomeScreen(
    navController: NavController,
) {
    val finishedSpendingViewModel: FinishedSpendingViewModel = koinViewModel()
    val addSpendingViewModel: AddSpendingViewModel = koinViewModel()
    val chartDisplayViewModel: ChartDisplayViewModel = koinViewModel()
    val chartConstructorViewModel: ChartConstructorViewModel = koinViewModel()

    PrimaryBackground(
        isDialogOpen = addSpendingViewModel.isDialogOpen
                || chartConstructorViewModel.isChartConstructorDialogOpen
    ) {
        ChartPanel(
            chartDisplayViewModel = chartDisplayViewModel,
            chartConstructorViewModel = chartConstructorViewModel
        )
        FinishedSpendingsPanel(
            viewModel = finishedSpendingViewModel,
            onAddSpendingClick = addSpendingViewModel::openDialog,
        )
        NavBar(navController = navController, backgroundColor = Colors.backgroundDark)
    }
    AddSpendingDialog(addSpendingViewModel)
    ChartConstructor(constructorViewModel = chartConstructorViewModel)
}