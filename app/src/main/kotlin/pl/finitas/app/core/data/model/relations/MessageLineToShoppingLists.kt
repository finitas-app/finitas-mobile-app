package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.MessageLine
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.core.data.model.TotalSpending
import pl.finitas.app.core.data.model.User

data class MessageLineToShoppingLists(
    @Embedded
    val messageLine: MessageLine,
    @Relation(
        parentColumn = "idMessageLine",
        entityColumn = "idMessageLine"
    )
    val shoppingLists: List<ShoppingList>,
)
