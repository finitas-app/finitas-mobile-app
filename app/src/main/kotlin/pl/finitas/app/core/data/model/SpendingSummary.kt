package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.util.UUID

@Entity
data class SpendingSummary(
    val name: String,
    val currencyValue: CurrencyValue,
    @PrimaryKey val idSpendingSummary: UUID,
)
