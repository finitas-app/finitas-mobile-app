package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class SpendingUnit(
    val name: String,
    val description: String,
    val price: BigDecimal,
    val idCategory: Int?,
    val idTotalSpending: Int?,
    @PrimaryKey val idSpendingUnit: Int,
)
