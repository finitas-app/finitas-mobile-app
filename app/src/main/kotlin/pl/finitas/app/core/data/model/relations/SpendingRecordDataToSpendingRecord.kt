package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData

data class SpendingRecordDataToSpendingRecord(
    @Embedded
    val spendingRecordData: SpendingRecordData,
    @Relation(
        parentColumn = "idSpendingRecordData",
        entityColumn = "idSpendingRecordData"
    )
    val spendingRecord: SpendingRecord,
)
