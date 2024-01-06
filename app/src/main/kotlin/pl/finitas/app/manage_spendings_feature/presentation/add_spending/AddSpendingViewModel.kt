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
import pl.finitas.app.manage_spendings_feature.domain.service.ScanReceiptService
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.time.LocalDate
import java.util.UUID

class AddSpendingViewModel(
    private val spendingCategoryService: SpendingCategoryService,
    private val finishedSpendingService: FinishedSpendingService,
    private val scanReceiptService: ScanReceiptService,
    private val settingsRepository: SettingsRepository,
): ViewModel() {

    var isDialogOpen by mutableStateOf(false)
        private set

    var finishedSpendingState by mutableStateOf(FinishedSpendingState.emptyState)
        private set

    fun openDialog(finishedSpendingView: FinishedSpendingView?, idUser: UUID?) {
        viewModelScope.launch {
            finishedSpendingState =
            if (finishedSpendingView == null) {
                val categories = spendingCategoryService.getSpendingCategoriesByIdUserFlat(idUser)
                FinishedSpendingState(categories = categories, idUser, settingsRepository.getDefaultCurrency().first())
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

    fun setCurrency(currencyValue: CurrencyValue) {
        finishedSpendingState = finishedSpendingState.copy(currencyValue = currencyValue)
    }

    fun deleteFinishedSpending(idFinishedSpending: UUID) {
        viewModelScope.launch {
            finishedSpendingService.deleteFinishedSpending(idFinishedSpending)
            closeDialog()
        }
    }

    fun processImage(file: ByteArray) {
        viewModelScope.launch {
            val currentCategories = finishedSpendingState.categories.toMutableList()
            val firstCategory = currentCategories.removeFirst()
            val firstCategoryWithParsedElements = firstCategory.copy(
                elements = firstCategory.elements + scanReceiptService.scanReceipt(
                    file,
                    firstCategory.idCategory
                )
            )
            finishedSpendingState = finishedSpendingState.copy(
                categories = currentCategories.apply {
                    add(0, firstCategoryWithParsedElements)
                }
            )
        }
    }
}