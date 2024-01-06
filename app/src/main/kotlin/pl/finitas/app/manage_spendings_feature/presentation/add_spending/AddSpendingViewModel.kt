package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService
import pl.finitas.app.manage_spendings_feature.domain.service.ScanReceiptService
import java.time.LocalDate
import java.util.UUID

class AddSpendingViewModel(
    private val spendingCategoryService: SpendingCategoryService,
    private val finishedSpendingService: FinishedSpendingService,
    private val scanReceiptService: ScanReceiptService
) : ViewModel() {

    var isDialogOpen by mutableStateOf(false)
        private set

    var finishedSpendingState by mutableStateOf(FinishedSpendingState.emptyState)
        private set

    fun openDialog(finishedSpendingView: FinishedSpendingView?) {
        viewModelScope.launch {
            val categories = spendingCategoryService.getSpendingCategoriesFlat()
            finishedSpendingState =
                if (finishedSpendingView == null) {
                    FinishedSpendingState(categories = categories)
                } else {
                    FinishedSpendingState(categories = categories, finishedSpendingView)
                }
            isDialogOpen = true
        }
    }

    fun closeDialog() {
        finishedSpendingState = FinishedSpendingState.emptyState
        isDialogOpen = false
    }

    init {
        viewModelScope.launch {
            finishedSpendingState = spendingCategoryService.getSpendingCategoriesFlat().let {
                finishedSpendingState.copy(categories = it)
            }
        }
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
            finishedSpendingService.upsertTotalSpending(finishedSpendingState)
            closeDialog()
        }
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