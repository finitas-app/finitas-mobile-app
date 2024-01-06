package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.DateInput
import pl.finitas.app.core.presentation.components.constructors.DeleteIcon
import pl.finitas.app.core.presentation.components.constructors.Dropdown
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.dialog.CustomDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.components.CategorySpendingList
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.components.ScanReceiptIcon
import pl.finitas.app.profile_feature.presentation.CurrencyValue

@Composable
fun AddSpendingDialog(
    addSpendingViewModel: AddSpendingViewModel,
) {
    CustomDialog(
        isOpened = addSpendingViewModel.isDialogOpen,
        onDismissRequest = addSpendingViewModel::closeDialog,
        onConfirmRequest = addSpendingViewModel::onSave,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        AddSpendingForm(addSpendingViewModel)
    }
}

@Composable
private fun AddSpendingForm(
    addSpendingViewModel: AddSpendingViewModel,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        AddSpendingFormGeneralInfo(
            addSpendingViewModel = addSpendingViewModel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
        )
        CategorySpendingList(
            categories = addSpendingViewModel.finishedSpendingState.categories,
            onDeleteElement = addSpendingViewModel::removeSpending,
            onSave = addSpendingViewModel::addSpending,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        )
    }
}

@Composable
private fun AddSpendingFormGeneralInfo(
    addSpendingViewModel: AddSpendingViewModel,
    modifier: Modifier = Modifier,
) {
    ConstructorBox(
        modifier = modifier,
        postModifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 30.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Fonts.heading1.Text(text = "New report")
                ScanReceiptIcon(
                    addSpendingViewModel,
                    Modifier.padding(top = 4.dp),
                )
            }

            Fonts.regular.Text(
                text = "Title",
                modifier = Modifier
                    .padding(top = 26.dp)
            )
            ConstructorInput(
                value = addSpendingViewModel.finishedSpendingState.title,
                onValueChange = addSpendingViewModel::setTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )
            InputError(errors = addSpendingViewModel.titleErrors, Modifier.padding(top = 18.dp))

            Fonts.regular.Text(
                text = "Date",
                modifier = Modifier
                    .padding(top = 18.dp)
            )
            DateInput(
                date = addSpendingViewModel.finishedSpendingState.date,
                onDateChange = addSpendingViewModel::setDate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
            Row(
                modifier = Modifier.padding(top = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Fonts.regular.Text(
                    text = "Currency",
                )
                Dropdown(
                    currentValue = addSpendingViewModel.finishedSpendingState.currencyValue,
                    values = CurrencyValue.entries,
                    onClick = addSpendingViewModel::setCurrency,
                    modifier = Modifier.padding(start = 15.dp, top = 4.dp)
                )
            }
            InputError(errors = addSpendingViewModel.errors, Modifier.padding(top = 18.dp))
            if (addSpendingViewModel.finishedSpendingState.idFinishedSpending != null) {
                DeleteIcon(
                    label = "Delete spending",
                    onDeleteClick = {
                        addSpendingViewModel.deleteFinishedSpending(
                            addSpendingViewModel.finishedSpendingState.idFinishedSpending!!
                        )
                    },
                    modifier = Modifier
                        .padding(top = 26.dp, end = 30.dp)
                        .align(Alignment.Start),
                )
            }
        }
    }
}
