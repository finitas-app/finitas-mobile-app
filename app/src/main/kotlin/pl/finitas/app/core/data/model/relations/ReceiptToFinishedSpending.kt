package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.Receipt
import pl.finitas.app.core.data.model.SpendingSummary

data class ReceiptToFinishedSpending(
    @Embedded
    val receipt: Receipt,
    @Relation(
        parentColumn = "idReceipt",
        entityColumn = "idReceipt"
    )
    val finishedSpending: FinishedSpending,
)