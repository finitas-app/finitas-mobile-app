package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class SpendingRecord(
    val idSpendingRecordData: String,
    val idFinishedSpending: String,
    @PrimaryKey val idSpendingRecord: String? = null,
)
