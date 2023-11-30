package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Category
import pl.finitas.app.core.data.model.SpendingUnit

data class CategoryToSpendingUnits(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    val spendingUnits: List<SpendingUnit>,
)