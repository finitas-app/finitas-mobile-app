package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.finitas.app.manage_spendings_feature.domain.services.TotalSpendingService


class TotalSpendingViewModel(
    private val totalSpendingService: TotalSpendingService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val totalSpendings = totalSpendingService.getTotalSpendings()
}