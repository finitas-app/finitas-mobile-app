package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.UUID

@Entity
data class SpendingRecord(
    val idSpendingSummary: UUID,
    val price: BigDecimal,
    @PrimaryKey val idSpendingRecordData: UUID,
)
