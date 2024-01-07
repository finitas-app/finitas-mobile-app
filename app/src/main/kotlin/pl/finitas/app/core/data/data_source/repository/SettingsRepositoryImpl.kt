package pl.finitas.app.core.data.data_source.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.DEFAULT_REGULAR_SPENDING_ACTUALIZATION_TIME
import pl.finitas.app.core.data.DEFAULT_REMINDER_NOTIFICATION_TIME
import pl.finitas.app.core.domain.repository.SettingsRepository
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.time.LocalTime

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val currencyKey = stringPreferencesKey("currency")
private val reminderNotificationStateKey = stringPreferencesKey("reminderNotificationState")
private val reminderNotificationTimeKey = stringPreferencesKey("reminderNotificationTime")
private val regularSpendingActualizationTimeKey = stringPreferencesKey("regularSpendingActualizationTime")

class SettingsRepositoryImpl(
    private val context: Context,
) : SettingsRepository {
    override suspend fun setDefaultCurrency(currencyValue: CurrencyValue) {
        context.dataStore.edit {
            it[currencyKey] = currencyValue.toString()
        }
    }

    override fun getDefaultCurrency(): Flow<CurrencyValue> {
        return context.dataStore.data.map { preferences ->
            preferences[currencyKey]
                ?.let { CurrencyValue.valueOf(it) }
                ?: CurrencyValue.PLN
        }
    }

    override suspend fun setReminderNotificationState(state: Boolean) {
        context.dataStore.edit {
            it[reminderNotificationStateKey] = state.toString()
        }
    }

    override fun getReminderNotificationState(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[reminderNotificationStateKey]
                ?.let { it.toBoolean() }
                ?: false
        }
    }

    override suspend fun setReminderNotificationTime(time: LocalTime) {
        context.dataStore.edit {
            it[reminderNotificationTimeKey] = time.toString()
        }
    }

    override fun getReminderNotificationTime(): Flow<LocalTime> {
        return context.dataStore.data.map { preferences ->
            preferences[reminderNotificationTimeKey]
                ?.let {
                    try {
                        LocalTime.parse(it)
                    } catch(_: Exception) {
                        DEFAULT_REMINDER_NOTIFICATION_TIME
                    }
                }
                ?: DEFAULT_REMINDER_NOTIFICATION_TIME
        }
    }

    override suspend fun setRegularSpendingActualizationTime(time: LocalTime) {
        context.dataStore.edit {
            it[regularSpendingActualizationTimeKey] = time.toString()
        }
    }

    override fun getRegularSpendingActualizationTime(): Flow<LocalTime> {
        return context.dataStore.data.map { preferences ->
            preferences[regularSpendingActualizationTimeKey]
                ?.let {
                    try {
                        LocalTime.parse(it)
                    } catch(_: Exception) {
                        DEFAULT_REGULAR_SPENDING_ACTUALIZATION_TIME
                    }
                }
                ?: DEFAULT_REGULAR_SPENDING_ACTUALIZATION_TIME
        }
    }
}