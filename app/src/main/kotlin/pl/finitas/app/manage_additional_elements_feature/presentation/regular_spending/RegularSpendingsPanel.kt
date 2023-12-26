package pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto
import pl.finitas.app.manage_additional_elements_feature.presentation.components.AdditionalElemHeader

val borderColor = Color.White.copy(alpha = .1f)

@Composable
fun RegularSpendingsPanel(
    regularSpendingsViewModel: RegularSpendingsViewModel,
    modifier: Modifier = Modifier,
) {
    val regularSpendings: List<RegularSpendingWithSpendingDataDto> by regularSpendingsViewModel.regularSpendings.collectAsState(
        initial = listOf()
    )

    Column(modifier = modifier) {
        AdditionalElemHeader(
            title = "Regular spendings",
            addIconOnClick = regularSpendingsViewModel::addIconOnClick
        )
        RegularSpendingsList(
            regularSpendings = regularSpendings,
            modifier = modifier,
            regularSpendingsViewModel = regularSpendingsViewModel,
        )
    }
}

@Composable
private fun RegularSpendingsList(
    regularSpendings: List<RegularSpendingWithSpendingDataDto>,
    modifier: Modifier = Modifier,
    regularSpendingsViewModel: RegularSpendingsViewModel
) {
    if (regularSpendings.isNotEmpty()) {
        ConstructorBox(Modifier.padding(top = 12.dp)) {
            Column(
                modifier
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    regularSpendings.forEachIndexed { index, spending ->
                        if (index != 0) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(borderColor)
                            )
                        }
                        RegularSpendingItemInList(
                            regularSpendingsViewModel = regularSpendingsViewModel,
                            currentSpending = spending
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RegularSpendingItemInList(
    regularSpendingsViewModel: RegularSpendingsViewModel,
    currentSpending: RegularSpendingWithSpendingDataDto,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Fonts.regular.Text(
            text = currentSpending.name,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
        )
        Icons(
            regularSpendingsViewModel = regularSpendingsViewModel,
            currentSpending = currentSpending
        )
    }
}

@Composable
private fun Icons(
    regularSpendingsViewModel: RegularSpendingsViewModel,
    currentSpending: RegularSpendingWithSpendingDataDto,
) {
    Row {
        ClickableIcon(
            imageVector = Icons.Rounded.Edit,
            onClick = {
                regularSpendingsViewModel.editButtonOnClick(
                    currentSpending
                )
            },
        )
        ClickableIcon(
            imageVector = Icons.Rounded.Delete,
            onClick = {
                // todo: add confirm popup
                regularSpendingsViewModel.deleteRegularSpending(
                    currentSpending
                )
            },
            modifier = Modifier.padding(end = 10.dp)
        )
    }
}