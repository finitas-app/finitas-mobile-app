package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.lifecycle.ViewModel
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService


class FinishedSpendingViewModel(
    private val finishedSpendingService: FinishedSpendingService,
) : ViewModel() {
    val totalSpendings = finishedSpendingService.getTotalSpendings()
}