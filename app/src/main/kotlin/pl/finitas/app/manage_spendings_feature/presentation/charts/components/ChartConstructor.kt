package pl.finitas.app.manage_spendings_feature.presentation.charts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.DateInput
import pl.finitas.app.core.presentation.components.constructors.Dropdown
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.constructors.LayeredList
import pl.finitas.app.core.presentation.components.dialog.CustomDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartType
import java.time.LocalDate

// todo: configure blur for dialog

@Composable
fun ChartConstructor(viewModel: ChartConstructorViewModel) {
    CustomDialog(
        isOpened = viewModel.isChartConstructorDialogOpen,
        onDismissRequest = viewModel::closeConstructor,
        onConfirmRequest = viewModel::upsertChart,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            GeneralInfo(viewModel = viewModel, modifier = Modifier.fillMaxWidth())
            CategoriesList(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GeneralInfo(
    modifier: Modifier = Modifier,
    viewModel: ChartConstructorViewModel
) {
    ConstructorBox(
        modifier = modifier,
        postModifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 30.dp)
    ) {
        Column {
            Fonts.heading1.Text(text = viewModel.constructorType.headerTitle)
            DiagramTypeInput(viewModel = viewModel)
            StartDateInput(viewModel = viewModel)
            EndDateInput(viewModel = viewModel)
            InputError(errors = viewModel.errors)
        }
    }
}

@Composable
private fun DiagramTypeInput(viewModel: ChartConstructorViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = 30.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        Fonts.regular.Text(text = "Diagram type")
        Dropdown(
            currentValue = viewModel.chartState.chartType,
            values = ChartType.entries,
            onClick = viewModel::setChartType,
            modifier = Modifier.width(80.dp)
        )
    }
}

@Composable
private fun StartDateInput(viewModel: ChartConstructorViewModel) {
    var startDateEnabled by remember { mutableStateOf(true) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Fonts.regular.Text(text = "From")

        Row(verticalAlignment = Alignment.CenterVertically) {
            Fonts.regularMini.Text(text = "without start date")

            ClickableIcon(
                imageVector =
                if (startDateEnabled) Icons.Rounded.CheckBoxOutlineBlank
                else Icons.Rounded.CheckBox,
                onClick = {
                    startDateEnabled = !startDateEnabled
                    if (!startDateEnabled) viewModel.setStartDate(null)
                    else viewModel.setStartDate(LocalDate.now())
                })
        }
    }


    DateInput(
        date = viewModel.chartState.startDate ?: LocalDate.now(),
        onDateChange = viewModel::setStartDate,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        enabled = startDateEnabled
    )
}

@Composable
private fun EndDateInput(viewModel: ChartConstructorViewModel) {
    var endDateEnabled by remember { mutableStateOf(true) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
    ) {
        Fonts.regular.Text(text = "To")

        Row(verticalAlignment = Alignment.CenterVertically) {
            Fonts.regularMini.Text(text = "without end date")

            ClickableIcon(
                imageVector =
                if (endDateEnabled) Icons.Rounded.CheckBoxOutlineBlank
                else Icons.Rounded.CheckBox,
                onClick = {
                    endDateEnabled = !endDateEnabled
                    if (!endDateEnabled) viewModel.setEndDate(null)
                    else viewModel.setEndDate(LocalDate.now())
                })
        }
    }

    DateInput(
        date = viewModel.chartState.endDate ?: LocalDate.now(),
        onDateChange = viewModel::setEndDate,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        enabled = endDateEnabled
    )
}

@Composable
private fun CategoriesList(
    viewModel: ChartConstructorViewModel,
    modifier: Modifier = Modifier
) {
    ConstructorBox(
        modifier = modifier,
        postModifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 30.dp)
    ) {
        Fonts.heading1.Text(text = "Categories")

        Box(modifier = Modifier.padding(top = 30.dp)) {
            LayeredList(
                nameableCollection = viewModel.chartState.categories,
                modifier = Modifier.padding(top = 20.dp)
            ) { spendingElement ->
                Box(modifier = Modifier.padding(end = 10.dp)) {
                    ClickableIcon(
                        imageVector =
                        if (spendingElement.idCategory in viewModel.enabledCategories) {
                            Icons.Rounded.CheckBox
                        } else Icons.Rounded.CheckBoxOutlineBlank,
                        onClick = {
                            viewModel.onCheckboxStateChanged(spendingElement)
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}