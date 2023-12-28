package pl.finitas.app.manage_spendings_feature.presentation.charts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel

// todo: configure the same smooth scrolling as in calendar

@Composable
fun ChartSlider(charts: List<ChartWithCategoriesDto>, viewModel: ChartConstructorViewModel) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(70.dp),
            contentPadding = PaddingValues(horizontal = 50.dp)
        ) {
            itemsIndexed(charts) { _, it ->
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    ChartElement(chart = it)
                    EditDeleteIcons(
                        chart = it,
                        viewModel = viewModel,
                        modifier = Modifier.align(alignment = Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
private fun EditDeleteIcons(
    chart: ChartWithCategoriesDto,
    viewModel: ChartConstructorViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(top = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ClickableIcon(
            imageVector = Icons.Rounded.Edit,
            onClick = {
                viewModel.onEditButtonClicked(chart)
            }
        )
        ClickableIcon(
            imageVector = Icons.Rounded.Delete,
            onClick = {
                viewModel.deleteChart(chart)
            }
        )
    }
}
