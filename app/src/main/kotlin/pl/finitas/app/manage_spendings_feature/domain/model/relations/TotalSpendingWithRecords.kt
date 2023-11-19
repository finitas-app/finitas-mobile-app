package pl.finitas.app.manage_spendings_feature.domain.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecord
import pl.finitas.app.manage_spendings_feature.domain.model.TotalSpending

data class TotalSpendingWithRecords(
    @Embedded val totalSpending: TotalSpending,
    @Relation(
        parentColumn = "idTotalSpending",
        entityColumn = "idTotalSpending"
    )
    val spendingRecords: List<SpendingRecord>,
)