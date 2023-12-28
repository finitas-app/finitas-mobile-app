package pl.finitas.app.manage_spendings_feature.presentation.charts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.domain.service.ChartService

class ChartDisplayViewModel(
    val service: ChartService,
) : ViewModel() {
    var charts = service.getChartsWithCategoriesFlow()
    var isChartConstructorDialogOpen by mutableStateOf(false)
        private set
}