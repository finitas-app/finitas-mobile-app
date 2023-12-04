package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.SpendingRecord

data class FinishedSpendingToSpendingRecords(
    @Embedded
    val finishedSpending: FinishedSpending,
    @Relation(
        parentColumn = "idFinishedSpending",
        entityColumn = "idFinishedSpending"
    )
    val spendingRecords: List<SpendingRecord>,
)
