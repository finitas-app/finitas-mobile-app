package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class RegularSpending(
    val actualizationPeriod: Int,
    val periodUnit: Int,
    val lastActualizationDate: LocalDateTime,
    @PrimaryKey val idSpendingSummary: UUID,
)
