package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.repository.SettingsRepository
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService
import pl.finitas.app.profile_feature.presentation.CurrencyValues
import java.time.LocalDate
import java.util.UUID

class AddSpendingViewModel(
    private val spendingCategoryService: SpendingCategoryService,
    private val finishedSpendingService: FinishedSpendingService,
    private val settingsRepository: SettingsRepository,
): ViewModel() {

    var currencyValue by mutableStateOf(CurrencyValues.PLN)
        private set

    var isDialogOpen by mutableStateOf(false)
        private set

    var finishedSpendingState by mutableStateOf(FinishedSpendingState.emptyState)
        private set

    init {
        viewModelScope.launch {
            currencyValue = settingsRepository.getDefaultCurrency().first() ?: CurrencyValues.PLN
        }
    }

    fun openDialog(finishedSpendingView: FinishedSpendingView?, idUser: UUID?) {
        viewModelScope.launch {
            finishedSpendingState =
            if (finishedSpendingView == null) {
                val categories = spendingCategoryService.getSpendingCategoriesByIdUserFlat(idUser)
                FinishedSpendingState(categories = categories, idUser)
            } else {
                val categories = spendingCategoryService.getSpendingCategoriesByIdUserFlat(idUser)
                FinishedSpendingState(categories = categories, finishedSpendingView)
            }
            isDialogOpen = true
        }
    }

    fun closeDialog() {
        finishedSpendingState = FinishedSpendingState.emptyState
        isDialogOpen = false
    }

    fun setTitle(title: String) {
        finishedSpendingState = finishedSpendingState.copy(title = title)
    }

    fun setDate(date: LocalDate) {
        finishedSpendingState = finishedSpendingState.copy(date = date)
    }

    fun addSpending(spendingRecord: SpendingRecordView) {
        finishedSpendingState = finishedSpendingState.addSpending(spendingRecord)
    }

    fun removeSpending(spendingRecord: SpendingRecordView) {
        finishedSpendingState = finishedSpendingState.removeSpending(spendingRecord)
    }

    fun onSave() {
        viewModelScope.launch {
            finishedSpendingService.upsertFinishedSpending(finishedSpendingState)
            closeDialog()
        }
    }

    fun setCurrency(currencyValue: CurrencyValues) {
        this.currencyValue = currencyValue
    }

    fun deleteFinishedSpending(idFinishedSpending: UUID) {
        viewModelScope.launch {
            finishedSpendingService.deleteFinishedSpending(idFinishedSpending)
            closeDialog()
        }
    }
}