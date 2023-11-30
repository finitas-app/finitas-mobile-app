package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Receipt
import pl.finitas.app.core.data.model.TotalSpending

data class TotalSpendingToReceipt(
    @Embedded
    val totalSpending: TotalSpending,
    @Relation(
        parentColumn = "idTotalSpending",
        entityColumn = "idTotalSpending"
    )
    val receipt: Receipt?,
)