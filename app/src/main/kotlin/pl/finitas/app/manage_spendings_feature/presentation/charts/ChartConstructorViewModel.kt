package pl.finitas.app.manage_spendings_feature.presentation.charts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.domain.service.ChartService
import java.time.LocalDate
import java.util.UUID

enum class ConstructorType(val headerTitle: String) {
    CREATE("Create chart"),
    EDIT("Edit chart")
}

class ChartConstructorViewModel(
    private val service: ChartService,
) : ViewModel() {
    var errors: List<String>? by mutableStateOf(null)
    var chartState by mutableStateOf(ChartState.empty())
        private set
    var isChartConstructorDialogOpen by mutableStateOf(false)
        private set
    var constructorType by mutableStateOf(ConstructorType.CREATE)
    var enabledCategories by mutableStateOf(setOf<UUID>())
        private set

    fun openCreateConstructor() {
        viewModelScope.launch {
            chartState = chartState.copy(categories = service.getSpendingCategories())
            constructorType = ConstructorType.CREATE
            isChartConstructorDialogOpen = true
        }
    }

    fun closeConstructor() {
        isChartConstructorDialogOpen = false
        chartState = ChartState.empty()
    }

    fun upsertChart() {
        viewModelScope.launch {
            try {
                service.upsertChart(chartState, enabledCategories)
                errors = null
                closeConstructor()
            } catch (exception: InputValidationException) {
                errors = exception.errors
            }
        }
    }

    fun setChartType(value: ChartType) {
        chartState = chartState.copy(chartType = value)
    }

    fun setStartDate(value: LocalDate?) {
        chartState = chartState.copy(startDate = value)
    }

    fun setEndDate(value: LocalDate?) {
        chartState = chartState.copy(endDate = value)
    }

    fun onCheckboxStateChanged(category: SpendingCategoryView) {
        val isEnabled = category.idCategory in enabledCategories

        enabledCategories = if (isEnabled) {
            enabledCategories - category.idCategory
        } else {
            getAllSpendingCategoryChildrenIds(category).apply {
                addAll(enabledCategories)
            }
        }
    }

    fun deleteChart(chart: ChartWithCategoriesDto) {
        viewModelScope.launch {
            service.deleteChartWithCategoryRefs(chart)
        }
    }

    fun onEditButtonClicked(chart: ChartWithCategoriesDto) {
        viewModelScope.launch {
            isChartConstructorDialogOpen = true
            constructorType = ConstructorType.EDIT
            enabledCategories = chart.categories.map { it.idCategory }.toSet()

            chartState = ChartState(
                chartId = chart.idChart,
                chartType = chart.chartType,
                startDate = chart.startDate,
                endDate = chart.endDate,
                categories = service.getSpendingCategories()
            )
        }
    }

    fun onEndDateCheckboxClicked() {
        if (chartState.endDate != null) setEndDate(null)
        else setEndDate(LocalDate.now())
    }

    fun onStartDateCheckboxClicked() {
        if (chartState.startDate != null) setStartDate(null)
        else setStartDate(LocalDate.now())
    }

    private fun getAllSpendingCategoryChildrenIds(spendingCategoryView: SpendingCategoryView): MutableSet<UUID> {
        val ids = mutableSetOf<UUID>()

        getAllSpendingCategoryChildrenIdsRecursively(spendingCategoryView, ids)

        return ids
    }

    private fun getAllSpendingCategoryChildrenIdsRecursively(
        category: SpendingCategoryView,
        set: MutableSet<UUID>
    ) {
        set.add(category.idCategory)

        if (category.elements.isEmpty()) {
            return
        }

        category.elements.forEach {
            getAllSpendingCategoryChildrenIdsRecursively(
                it as SpendingCategoryView,
                set
            )
        }
    }
}