package pl.finitas.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.time.LocalTime

interface SettingsRepository {
    suspend fun setDefaultCurrency(currencyValue: CurrencyValue)
    fun getDefaultCurrency(): Flow<CurrencyValue>
    suspend fun setReminderNotificationState(state: Boolean)
    fun getReminderNotificationState(): Flow<Boolean>
    suspend fun setReminderNotificationTime(time: LocalTime)
    fun getReminderNotificationTime(): Flow<LocalTime>
    suspend fun setRegularSpendingActualizationTime(time: LocalTime)
    fun getRegularSpendingActualizationTime(): Flow<LocalTime>
}