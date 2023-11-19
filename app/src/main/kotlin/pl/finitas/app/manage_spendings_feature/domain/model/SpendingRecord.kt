package pl.finitas.app.manage_spendings_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class SpendingRecord(
    val name: String,
    val price: BigDecimal,
    val idCategory: Int,
    @PrimaryKey val idSpendingRecord: Int? = null,
    val idTotalSpending: Int? = null,
)
