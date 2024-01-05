package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService
import java.time.LocalDate
import java.util.UUID


class FinishedSpendingViewModel(
    private val finishedSpendingService: FinishedSpendingService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val totalSpendings: Flow<List<Pair<LocalDate, List<FinishedSpendingView>>>>

    init {
        val idsUserRaw = savedStateHandle.get<String>("idsUser")
        val idsUser = if (idsUserRaw.isNullOrBlank()) listOf()
        else idsUserRaw.split(",").map { UUID.fromString(it)!! }

        totalSpendings = finishedSpendingService.getTotalSpendings(idsUser)
    }
}