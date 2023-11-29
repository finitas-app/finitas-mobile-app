package pl.finitas.app.manage_spendings_feature.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingDialog
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingsPanel

@Composable
fun HomeScreen() {
    PrimaryBackground {
        val totalSpendingViewModel: TotalSpendingViewModel = koinViewModel()
        val addSpendingViewModel: AddSpendingViewModel = koinViewModel()
        val animatedBlur by animateDpAsState(
            targetValue = if (addSpendingViewModel.isDialogOpen) 15.dp else 0.dp,
            label = "blurAnimation",
        )

        Box(Modifier.fillMaxSize().blur(animatedBlur)) {
            TotalSpendingsPanel(
                viewModel = totalSpendingViewModel,
                onAddSpendingClick = addSpendingViewModel::openDialog,
            )
        }
        AddSpendingDialog(addSpendingViewModel)
    }
}