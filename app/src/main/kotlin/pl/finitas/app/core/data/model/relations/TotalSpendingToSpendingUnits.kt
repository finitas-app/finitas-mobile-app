package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.SpendingUnit
import pl.finitas.app.core.data.model.TotalSpending

data class TotalSpendingToSpendingUnits(
    @Embedded
    val totalSpending: TotalSpending,
    @Relation(
        parentColumn = "idTotalSpending",
        entityColumn = "idTotalSpending"
    )
    val spendingUnits: List<SpendingUnit>,
)
