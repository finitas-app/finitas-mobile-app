package pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.manage_additional_elements_feature.domain.services.RegularSpendingActualizationService

class RegularSpendingActualizationViewModel(
    private val service: RegularSpendingActualizationService,
) : ViewModel() {
    init {
        viewModelScope.launch {
            service.launchActualization()
        }
    }
}