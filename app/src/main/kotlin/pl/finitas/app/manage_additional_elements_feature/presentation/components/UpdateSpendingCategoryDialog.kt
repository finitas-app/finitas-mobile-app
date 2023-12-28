package pl.finitas.app.manage_additional_elements_feature.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.DeleteIcon
import pl.finitas.app.core.presentation.components.dialog.CustomDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryViewModel

@Composable
fun UpdateSpendingCategoryDialog(
    viewModel: SpendingCategoryViewModel,
) {
    CustomDialog(
        isOpened = viewModel.isUpsertCategoryDialogOpen,
        onDismissRequest = viewModel::closeUpsertCategoryDialog,
        onConfirmRequest = viewModel::save,
        alignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                end = 10.dp,
                bottom = 100.dp,
                start = 10.dp,
            )
        ,
    ) {
        UpdateSpendingCategoryPanel(viewModel)
    }
}

@Composable
private fun UpdateSpendingCategoryPanel(
    viewModel: SpendingCategoryViewModel,
) {
    ConstructorBox(
        modifier = Modifier
            .fillMaxWidth()
            ,
        postModifier = Modifier
            .padding(20.dp)
    ) {
        Column {
            Fonts.heading1.Text(text = "New Category")
            Fonts.regular.Text(
                text = "Title",
                Modifier.padding(top = 20.dp)
            )
            ConstructorInput(
                value = viewModel.spendingCategoryState.name,
                onValueChange = viewModel::setCategoryName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )

            if (viewModel.spendingCategoryState.idCategory != null) {
                DeleteIcon(
                    label = "Delete spending",
                    onDeleteClick = {
                        // TODO: Delete category
                    },
                    modifier = Modifier
                        .padding(top = 26.dp, end = 30.dp)
                        .align(Alignment.Start),
                )
            }
        }
    }
}