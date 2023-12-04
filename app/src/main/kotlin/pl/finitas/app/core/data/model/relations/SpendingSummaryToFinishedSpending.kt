package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingSummary

data class SpendingSummaryToFinishedSpending(
    @Embedded
    val spendingSummary: SpendingSummary,
    @Relation(
        parentColumn = "idSpendingSummary",
        entityColumn = "idSpendingSummary"
    )
    val finishedSpending: FinishedSpending,
)