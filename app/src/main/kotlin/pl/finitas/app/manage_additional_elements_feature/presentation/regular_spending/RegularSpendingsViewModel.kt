package pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto
import pl.finitas.app.manage_additional_elements_feature.domain.services.RegularSpendingService

enum class ConstructorAction(val headerText: String) {
    CREATE("New regular spending"),
    EDIT("Edit regular spending"),
}

class RegularSpendingsViewModel(
    private val spendingCategoryService: SpendingCategoryService,
    private val regularSpendingService: RegularSpendingService,
) : ViewModel() {

    var isDialogOpen by mutableStateOf(false)
        private set

    var errors: List<String>? by mutableStateOf(null)

    var constructorAction by mutableStateOf(ConstructorAction.EDIT)

    val regularSpendings = regularSpendingService.getRegularSpendingsFlow()

    var regularSpendingState by mutableStateOf(RegularSpendingState())
        private set

    fun editButtonOnClick(currentSpending: RegularSpendingWithSpendingDataDto) {
        viewModelScope.launch {
            regularSpendingState = regularSpendingService.getStateFrom(
                regularSpending = currentSpending,
                allCategories = spendingCategoryService.getSpendingCategoriesFlat()
            )
            constructorAction = ConstructorAction.EDIT
            isDialogOpen = true
        }
    }

    fun addIconOnClick() {
        viewModelScope.launch {
            regularSpendingState = regularSpendingState.copy(
                categories = spendingCategoryService.getSpendingCategoriesFlat()
            )
            constructorAction = ConstructorAction.CREATE
            isDialogOpen = true
        }
    }

    fun closeDialog() {
        regularSpendingState = RegularSpendingState()
        isDialogOpen = false
    }

    fun setStateTitle(value: String) {
        regularSpendingState = regularSpendingState.copy(title = value)
    }

    fun onEditorSave() {
        viewModelScope.launch {
            try {
                regularSpendingService.upsertRegularSpendingWithRecords(regularSpendingState)
                errors = null
                closeDialog()
            } catch (inputError: InputValidationException) {
                errors = inputError.errors
            }
        }
    }

    fun deleteRegularSpending(regularSpending: RegularSpendingWithSpendingDataDto) {
        viewModelScope.launch {
            regularSpendingService.deleteRegularSpendingWithRecords(regularSpending)
        }
    }

    fun addSpendingRecord(view: SpendingRecordView) {
        regularSpendingState = regularSpendingState.addSpending(view)
    }

    fun removeSpendingRecord(view: SpendingRecordView) {
        regularSpendingState = regularSpendingState.removeSpending(view)
    }

    fun setActualizationPeriod(value: UInt) {
        regularSpendingState = regularSpendingState.copy(actualizationPeriod = value)
    }

    fun setPeriodUnit(value: PeriodUnit) {
        regularSpendingState = regularSpendingState.copy(periodUnit = value)
    }
}
