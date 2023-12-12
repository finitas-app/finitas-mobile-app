package pl.finitas.app.manage_additional_elements_feature.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.manage_additional_elements_feature.presentation.components.UpdateSpendingCategoryDialog
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryPanel
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryViewModel

@Composable
fun AdditionalElementsScreen(
    navController: NavController,
) {
    val spendingCategoryViewModel: SpendingCategoryViewModel = koinViewModel()
    PrimaryBackground(
        isDialogOpen = spendingCategoryViewModel.isUpsertCategoryDialogOpen,
    ) {
        Column {
            SpendingCategoryPanel(
                viewModel = spendingCategoryViewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
            )
        }
        NavBar(navController)
    }
    UpdateSpendingCategoryDialog(viewModel = spendingCategoryViewModel)
}
