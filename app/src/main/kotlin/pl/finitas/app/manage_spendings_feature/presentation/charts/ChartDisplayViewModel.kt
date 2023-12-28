package pl.finitas.app.manage_spendings_feature.presentation.charts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pl.finitas.app.manage_spendings_feature.domain.service.ChartService

class ChartDisplayViewModel(
    val service: ChartService,
) : ViewModel() {
    var charts = service.getChartsWithCategoriesFlow()
}