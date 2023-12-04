package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FinishedSpending(
    val idReceipt: String?,
    val purchaseDate: Int,
    val idUser: String?,
    @PrimaryKey val idSpendingSummary: String? = null,
)
