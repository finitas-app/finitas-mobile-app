package pl.finitas.app.manage_spendings_feature.presentation.charts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel

@Composable
fun ChartSlider(
    charts: List<ChartWithCategoriesDto>,
    constructorViewModel: ChartConstructorViewModel,
) {
    val listState = rememberLazyListState(0)
    val positionOfList by remember { derivedStateOf { ItemPositions(listState) } }


    LaunchedEffect(positionOfList) {
        val (index, offset, isScrolling) = positionOfList

        if (!isScrolling && offset != 0) {
            listState.animateScrollToItem(index, 0)
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        LazyRow(
            state = listState,
        ) {
            this.items(charts.size) { index ->
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    ChartElement(
                        chart = charts[index],
                        modifier = Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp)
                            .weight(.8f)
                            .padding(horizontal = 20.dp)
                    )
                    EditDeleteIcons(
                        chart = charts[index],
                        viewModel = constructorViewModel,
                        modifier = Modifier
                            .align(alignment = Alignment.End)
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
    modifier: Modifier = Modifier,
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

private data class ItemPositions(
    val index: Int,
    val offset: Int,
    val isScrolling: Boolean,
) {

    constructor(listState: LazyListState) : this(
        listState.firstVisibleItemIndex,
        listState.firstVisibleItemScrollOffset,
        listState.isScrollInProgress,
    )
}
