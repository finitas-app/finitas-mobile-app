package pl.finitas.app.profile_feature.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.repository.SettingsRepository
import pl.finitas.app.profile_feature.domain.services.ProfileService
import java.time.LocalTime

class ProfileViewModel(
    private val profileService: ProfileService,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val currency: Flow<CurrencyValue> = settingsRepository.getDefaultCurrency()
    val regularSpendingActualizationTime = settingsRepository.getRegularSpendingActualizationTime()
    val isAuthorize = profileService.getToken().map { it != null }
    var usernameErrors by mutableStateOf<List<String>?>(null)
        private set


    val username = profileService.getUsername()

    fun setVisibleName(value: String) {
        viewModelScope.launch {
            usernameErrors = try {
                profileService.setUsername(value)
                null
            } catch (e: InputValidationException) {
                e.errors[null]
            } catch (e: Exception) {
                listOf("Failed to change name, check your internet connection")
            }
        }
    }

    fun setCurrency(value: CurrencyValue) {
        viewModelScope.launch {
            settingsRepository.setDefaultCurrency(value)
        }
    }

    fun setRegularSpendingActualizationTime(value: LocalTime) {
        viewModelScope.launch {
            settingsRepository.setRegularSpendingActualizationTime(value)
        }
    }

    fun logout() {
        viewModelScope.launch {
            profileService.logout()
        }
    }
}