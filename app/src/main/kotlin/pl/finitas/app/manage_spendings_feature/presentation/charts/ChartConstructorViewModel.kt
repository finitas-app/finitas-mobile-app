package pl.finitas.app.manage_spendings_feature.presentation.charts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.repository.SettingsRepository
import pl.finitas.app.core.domain.services.AuthorizedUserService
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.domain.service.ChartService
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.time.LocalDate
import java.util.UUID

enum class ConstructorType(val headerTitle: String) {
    CREATE("Create chart"),
    EDIT("Edit chart")
}

class ChartConstructorViewModel(
    private val service: ChartService,
    private val authorizedUserService: AuthorizedUserService,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    var errors: List<String>? by mutableStateOf(null)

    var chartState by mutableStateOf(ChartState.empty())
        private set

    var isChartConstructorDialogOpen by mutableStateOf(false)
        private set

    var constructorType by mutableStateOf(ConstructorType.CREATE)

    var enabledCategories by mutableStateOf(setOf<UUID>())
        private set

    fun openCreateConstructor(idRoom: UUID?, idTargetUser: UUID?) {
        viewModelScope.launch {
            val authorizedUser = authorizedUserService.getAuthorizedIdUser().first()
            val targetUserOrCurrent =
                if (authorizedUser == idTargetUser)
                    null
                else {
                    idTargetUser
                }
            chartState = ChartState.empty().copy(
                categories = service.getSpendingCategories(idRoom, targetUserOrCurrent),
                idTargetUser = idTargetUser,
                idRoom = idRoom,
                currencyValue = settingsRepository.getDefaultCurrency().first()
            )
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
                errors = exception.errors[null]
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

    fun setCurrency(currencyValue: CurrencyValue) {
        chartState = chartState.copy(currencyValue = currencyValue)
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
                currencyValue = chart.currencyValue,
                categories = service.getSpendingCategories(chart.idRoom, chart.idTargetUser),
                idTargetUser = chart.idTargetUser,
                idRoom = chart.idRoom,
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
        set: MutableSet<UUID>,
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
