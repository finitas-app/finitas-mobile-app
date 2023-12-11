package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.UUID

@Entity
data class SpendingRecordData(
    val name: String,
    val price: BigDecimal,
    val idCategory: UUID,
    @PrimaryKey val idSpendingRecordData: UUID,
)
