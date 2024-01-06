package pl.finitas.app.core.data.data_source.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.domain.repository.SettingsRepository
import pl.finitas.app.profile_feature.presentation.CurrencyValues

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val currencyKey = stringPreferencesKey("currency")

class SettingsRepositoryImpl(
    private val context: Context,
) : SettingsRepository {
    override suspend fun setDefaultCurrency(currencyValue: CurrencyValues) {
        context.dataStore.edit {
            it[currencyKey] = currencyValue.toString()
        }
    }

    override fun getDefaultCurrency(): Flow<CurrencyValues> {
        return context.dataStore.data.map { preferences ->
            preferences[currencyKey]
                ?.let { CurrencyValues.valueOf(it) }
                ?: CurrencyValues.PLN
        }
    }
}