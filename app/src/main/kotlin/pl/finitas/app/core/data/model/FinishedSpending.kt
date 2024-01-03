package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = User::class,
            parentColumns = ["idUser"],
            childColumns = ["idUser"]
        ),
        ForeignKey(
            onDelete = ForeignKey.SET_NULL,
            entity = Receipt::class,
            parentColumns = ["idReceipt"],
            childColumns = ["idReceipt"]
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = SpendingSummary::class,
            parentColumns = ["idSpendingSummary"],
            childColumns = ["idSpendingSummary"]
        ),
    ],
)
data class FinishedSpending(
    @ColumnInfo(index = true)
    val idReceipt: UUID?,
    val purchaseDate: LocalDateTime,
    @ColumnInfo(index = true)
    val idUser: UUID?,
    @PrimaryKey val idSpendingSummary: UUID,
    val isDeleted: Boolean,
    val version: Int? = null,
)
