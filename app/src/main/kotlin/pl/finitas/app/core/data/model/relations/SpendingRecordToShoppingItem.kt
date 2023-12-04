package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.ShoppingItem
import pl.finitas.app.core.data.model.SpendingRecordData


// todo: should I put nullable for shoppingItem
data class SpendingRecordDataToShoppingItem(
    @Embedded
    val spendingRecordData: SpendingRecordData,
    @Relation(
        parentColumn = "idSpendingRecordData",
        entityColumn = "idSpendingRecordData"
    )
    val shoppingItem: ShoppingItem,
)