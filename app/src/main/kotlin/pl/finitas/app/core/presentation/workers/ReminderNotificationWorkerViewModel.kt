package pl.finitas.app.core.presentation.workers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.profile_feature.domain.services.NotificationPusherService
import java.time.LocalTime

class ReminderNotificationWorkerViewModel(
    private val notificationService: NotificationPusherService,
) : ViewModel() {
    fun launchNotifications(value: LocalTime) {
        viewModelScope.launch {
            notificationService.launchNotifications(value)
        }
    }
    fun cancelNotifications() {
        viewModelScope.launch {
            notificationService.cancelNotifications()
        }
    }
}
