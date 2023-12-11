package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.core.data.model.ShoppingList

data class ShoppingListToMessage(
    @Embedded
    val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "idShoppingList",
        entityColumn = "idShoppingList"
    )
    val roomMessage: RoomMessage,
)
