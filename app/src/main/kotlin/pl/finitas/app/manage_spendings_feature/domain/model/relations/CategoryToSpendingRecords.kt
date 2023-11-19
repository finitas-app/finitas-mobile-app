package pl.finitas.app.manage_spendings_feature.domain.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingCategory
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecord

data class CategoryToSpendingRecords(
    @Embedded
    val spendingCategory: SpendingCategory,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    val spendingRecords: List<SpendingRecord>,
)