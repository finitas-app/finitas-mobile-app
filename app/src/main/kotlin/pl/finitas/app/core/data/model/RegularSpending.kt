package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class RegularSpending(
    val cron: String,
    val idSpendingSummary: UUID,
    @PrimaryKey val idRegularSpending: UUID,
)
