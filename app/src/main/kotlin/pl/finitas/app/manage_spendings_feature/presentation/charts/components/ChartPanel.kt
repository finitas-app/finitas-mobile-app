package pl.finitas.app.manage_spendings_feature.presentation.charts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartDisplayViewModel

@Composable
fun ChartPanel(
    chartDisplayViewModel: ChartDisplayViewModel,
    chartConstructorViewModel: ChartConstructorViewModel,
) {
    val charts by chartDisplayViewModel.charts.collectAsState(initial = listOf())

    Column(modifier = Modifier.fillMaxHeight(.55f)) {
        ClickableIcon(
            imageVector = Icons.Rounded.AddCircle,
            onClick = chartConstructorViewModel::openCreateConstructor,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 10.dp, end = 10.dp)
        )
        ChartSlider(
            charts = charts,
            constructorViewModel = chartConstructorViewModel,
        )
    }
}
