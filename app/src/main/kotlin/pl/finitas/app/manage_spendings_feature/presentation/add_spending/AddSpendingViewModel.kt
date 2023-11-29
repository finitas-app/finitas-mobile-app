package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryService
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingRecordView
import java.time.LocalDate

class AddSpendingViewModel(
    private val spendingCategoryService: SpendingCategoryService,
): ViewModel() {

    var isDialogOpen by mutableStateOf(true)
        private set

    var totalSpendingState by mutableStateOf(TotalSpendingState(
        title = "",
        date = LocalDate.now(),
        categories = listOf(),
    ))
        private set


    fun openDialog() { isDialogOpen = true }
    fun closeDialog() { isDialogOpen = false }

    init {
        viewModelScope.launch {
            totalSpendingState = spendingCategoryService.getSpendingCategories().let {
                totalSpendingState.copy(categories = it)
            }
        }
    }

    fun setTitle(title: String) {
        totalSpendingState = totalSpendingState.copy(title = title)
    }

    fun setDate(date: LocalDate) {
        totalSpendingState = totalSpendingState.copy(date = date)
    }

    fun addSpending(spendingRecord: SpendingRecordView) {
        totalSpendingState = totalSpendingState.addSpending(spendingRecord)
    }

    fun removeSpending(spendingRecord: SpendingRecordView) {
        totalSpendingState = totalSpendingState.removeSpending(spendingRecord)
    }
}