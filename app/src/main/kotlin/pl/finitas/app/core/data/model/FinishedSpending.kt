package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class FinishedSpending(
    val idReceipt: UUID?,
    val purchaseDate: LocalDateTime,
    val idUser: UUID?,
    @PrimaryKey val idSpendingSummary: UUID,
)
