package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.ShoppingItem
import pl.finitas.app.core.data.model.SpendingUnit

data class SpendingUnitToShoppingItem(
    @Embedded
    val spendingUnit: SpendingUnit,
    @Relation(
        parentColumn = "idSpendingUnit",
        entityColumn = "idSpendingUnit"
    )
    val shoppingItem: ShoppingItem?,
)