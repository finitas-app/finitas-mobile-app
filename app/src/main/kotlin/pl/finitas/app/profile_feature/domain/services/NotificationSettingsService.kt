package pl.finitas.app.profile_feature.domain.services

import pl.finitas.app.core.domain.repository.SettingsRepository
import java.time.LocalTime

class NotificationSettingsService(private val repository: SettingsRepository) {
    suspend fun setReminderNotificationState(state: Boolean) = repository.setReminderNotificationState(state)
    fun getReminderNotificationState() = repository.getReminderNotificationState()
    suspend fun setReminderNotificationTime(time: LocalTime) = repository.setReminderNotificationTime(time)
    fun getReminderNotificationTime() = repository.getReminderNotificationTime()
}