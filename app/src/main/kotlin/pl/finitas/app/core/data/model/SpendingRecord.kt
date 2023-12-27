package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = SpendingSummary::class,
            parentColumns = ["idSpendingSummary"],
            childColumns = ["idSpendingSummary"]
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = SpendingRecordData::class,
            parentColumns = ["idSpendingRecordData"],
            childColumns = ["idSpendingRecordData"]
        ),
    ],
)
data class SpendingRecord(
    @ColumnInfo(index = true)
    val idSpendingSummary: UUID,
    val price: BigDecimal,
    @PrimaryKey val idSpendingRecordData: UUID,
)
