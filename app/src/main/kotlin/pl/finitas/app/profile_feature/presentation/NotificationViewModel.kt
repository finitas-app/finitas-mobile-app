package pl.finitas.app.profile_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.profile_feature.domain.services.NotificationSettingsService
import java.time.LocalTime

class NotificationPushViewModel(
    private val settingsService: NotificationSettingsService,
) : ViewModel() {
    val reminderPushState = settingsService.getReminderNotificationState()
    val reminderPushTime = settingsService.getReminderNotificationTime()

    fun setReminderNotificationsOn(value: Boolean) {
        viewModelScope.launch {
            println("*******************************")
            println("SWITCHING NOTIFICATION STATE. VALUE $value")
            settingsService.setReminderNotificationState(value)
        }
    }

    fun setReminderNotificationsTime(value: LocalTime) {
        viewModelScope.launch {
            println("*******************************")
            println("SETTING NOTIFICATION TIME. VALUE $value")
            settingsService.setReminderNotificationTime(value)
        }
    }
}