package pl.finitas.app.core.presentation.workers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.services.RegularSpendingActualizationService
import java.time.LocalTime

class RegularSpendingActualizationViewModel(
    private val service: RegularSpendingActualizationService,
) : ViewModel() {
    fun launchActualization(time: LocalTime) {
        viewModelScope.launch {
            service.launchActualization(time)
        }
    }
}