package pl.finitas.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.profile_feature.presentation.CurrencyValues

interface SettingsRepository {
    suspend fun setDefaultCurrency(currencyValue: CurrencyValues)

    fun getDefaultCurrency(): Flow<CurrencyValues?>
}