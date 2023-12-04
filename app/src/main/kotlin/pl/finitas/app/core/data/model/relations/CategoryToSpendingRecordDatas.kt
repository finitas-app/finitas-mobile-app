package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Category
import pl.finitas.app.core.data.model.SpendingRecordData

data class CategoryToSpendingRecordDatas(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    val spendingRecordDatas: List<SpendingRecordData>,
)