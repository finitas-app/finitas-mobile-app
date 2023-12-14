package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService


class TotalSpendingViewModel(
    private val finishedSpendingService: FinishedSpendingService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val totalSpendings = finishedSpendingService.getTotalSpendings()
}