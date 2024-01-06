package pl.finitas.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.profile_feature.presentation.CurrencyValue

interface SettingsRepository {
    suspend fun setDefaultCurrency(currencyValue: CurrencyValue)

    fun getDefaultCurrency(): Flow<CurrencyValue>
}