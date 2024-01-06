package pl.finitas.app.manage_additional_elements_feature.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.manage_additional_elements_feature.presentation.components.UpdateSpendingCategoryDialog
import pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending.EditRegularSpendingDialog
import pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending.RegularSpendingsPanel
import pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending.RegularSpendingsViewModel
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryPanel
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryViewModel

@Composable
fun AdditionalElementsScreen(
    navController: NavController,
) {
    val spendingCategoryViewModel: SpendingCategoryViewModel = koinViewModel()
    val regularSpendingViewModel: RegularSpendingsViewModel = koinViewModel()

    PrimaryBackground(
        isDialogOpen = spendingCategoryViewModel.isUpsertCategoryDialogOpen
                || regularSpendingViewModel.isDialogOpen,
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            val panelModifier = Modifier
                .fillMaxWidth()
                .padding(26.dp)

            SpendingCategoryPanel(
                viewModel = spendingCategoryViewModel,
                modifier = panelModifier,
            )
            RegularSpendingsPanel(
                regularSpendingsViewModel = regularSpendingViewModel,
                modifier = panelModifier,
            )
        }
        NavBar(navController)
    }
    EditRegularSpendingDialog(viewModel = regularSpendingViewModel)
    UpdateSpendingCategoryDialog(viewModel = spendingCategoryViewModel)
}
