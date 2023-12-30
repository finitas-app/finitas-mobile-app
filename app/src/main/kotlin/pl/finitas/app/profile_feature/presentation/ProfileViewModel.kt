package pl.finitas.app.profile_feature.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.finitas.app.profile_feature.domain.services.ProfileService

class ProfileViewModel(
    private val profileService: ProfileService,
) : ViewModel() {

    val isAuthorize = profileService.getToken().map { it != null }

    var profileSettingsState by mutableStateOf(ProfileSettingsState.empty)
        private set

    val username = profileService.getUsername()

    fun setVisibleName(value: String) {
        viewModelScope.launch {
            profileService.setUsername(value)
        }
    }

    fun setCurrency(value: CurrencyValues) {
        profileSettingsState = profileSettingsState.copy(currency = value)
    }

    fun setRegularSpendingActualizationNotificationsOn(value: Boolean) {
        val curState = profileSettingsState.regularSpendingsSettingsState
        profileSettingsState =
            profileSettingsState.copy(
                regularSpendingsSettingsState = curState.copy(
                    actualizationNotificationsOn = value
                )
            )
    }

    fun setRegularSpendingActualizationTimeHours(value: Int) {
        val curState = profileSettingsState.regularSpendingsSettingsState
        profileSettingsState =
            profileSettingsState.copy(
                regularSpendingsSettingsState = curState.copy(
                    actualizationTime = curState.actualizationTime.copy(
                        hours = value
                    )
                )
            )
    }

    fun setRegularSpendingActualizationTimeMinutes(value: Int) {
        val curState = profileSettingsState.regularSpendingsSettingsState
        profileSettingsState =
            profileSettingsState.copy(
                regularSpendingsSettingsState = curState.copy(
                    actualizationTime = curState.actualizationTime.copy(
                        minutes = value
                    )
                )
            )
    }

    fun setReminderNotificationsOn(value: Boolean) {
        val curState = profileSettingsState.reminderSettingsState
        profileSettingsState =
            profileSettingsState.copy(
                reminderSettingsState = curState.copy(
                    isNotificationsOn = value
                )
            )
    }

    fun setReminderNotificationsTimeMinutes(value: Int) {
        val curState = profileSettingsState.reminderSettingsState
        profileSettingsState =
            profileSettingsState.copy(
                reminderSettingsState = curState.copy(
                    notificationTime = curState.notificationTime.copy(
                        minutes = value
                    )
                )
            )
    }

    fun setReminderNotificationsTimeHours(value: Int) {
        val curState = profileSettingsState.reminderSettingsState
        profileSettingsState =
            profileSettingsState.copy(
                reminderSettingsState = curState.copy(
                    notificationTime = curState.notificationTime.copy(
                        hours = value
                    )
                )
            )
    }

    fun logout() {
        viewModelScope.launch {
            profileService.logout()
        }
    }
}