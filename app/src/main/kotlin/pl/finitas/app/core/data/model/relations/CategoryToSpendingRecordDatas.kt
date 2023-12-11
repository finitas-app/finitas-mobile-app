package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.data.model.SpendingRecordData

data class CategoryToSpendingRecordDatas(
    @Embedded
    val spendingCategory: SpendingCategory,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    val spendingRecordDatas: List<SpendingRecordData>,
)