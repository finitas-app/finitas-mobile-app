package pl.finitas.app.manage_spendings_feature.presentation

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingsPanel

@Composable
fun HomeScreen() {
    PrimaryBackground {
        val totalSpendingViewModel: TotalSpendingViewModel = koinViewModel()
        TotalSpendingsPanel(
            viewModel = totalSpendingViewModel,
        )
    }
}