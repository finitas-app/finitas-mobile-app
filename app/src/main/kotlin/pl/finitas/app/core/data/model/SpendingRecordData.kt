package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class SpendingRecordData(
    val name: String,
    val price: BigDecimal,
    val idCategory: String,
    val idFinishedSpending: String?,
    @PrimaryKey val idSpendingRecordData: String? = null,
)
