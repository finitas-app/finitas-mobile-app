package pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.Dropdown
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.dialog.CustomDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit

@Composable
fun EditRegularSpendingDialog(
    viewModel: RegularSpendingsViewModel,
) {
    CustomDialog(
        isOpened = viewModel.isDialogOpen,
        onDismissRequest = viewModel::closeDialog,
        onConfirmRequest = viewModel::onEditorSave,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        RegularSpendingForm(viewModel)
    }
}

@Composable
private fun RegularSpendingForm(viewModel: RegularSpendingsViewModel) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        FormGeneralInfo(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
        )
        CategorySpendingList(
            categories = viewModel.regularSpendingState.categories,
            onDeleteElement = viewModel::removeSpendingRecord,
            onSave = viewModel::addSpendingRecord,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        )
    }
}

@Composable
private fun FormGeneralInfo(
    viewModel: RegularSpendingsViewModel,
    modifier: Modifier = Modifier,
) {
    ConstructorBox(
        modifier = modifier,
        postModifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 30.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
            ) {
                Fonts.heading1.Text(text = viewModel.constructorAction.headerText)
            }

            Fonts.regular.Text(
                text = "Title",
                modifier = Modifier
                    .padding(top = 26.dp)
            )
            ConstructorInput(
                value = viewModel.regularSpendingState.title,
                onValueChange = viewModel::setStateTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )

            ActualizationPeriodInput(viewModel = viewModel)

            InputError(viewModel.errors, Modifier.padding(top = 26.dp))
        }
    }
}

@Composable
private fun ActualizationPeriodInput(viewModel: RegularSpendingsViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)
    ) {
        Fonts.regular.Text(text = "Period")

        Row(verticalAlignment = Alignment.CenterVertically) {
            // todo: center text in input
            ConstructorInput(
                value = viewModel.regularSpendingState.actualizationPeriod,
                onValueChange = viewModel::setActualizationPeriod,
                modifier = Modifier.width(80.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Dropdown(
                modifier = Modifier.width(80.dp),
                currentValue = viewModel.regularSpendingState.periodUnit,
                values = PeriodUnit.entries,
                onClick = viewModel::setPeriodUnit,
            )
        }
    }
}
