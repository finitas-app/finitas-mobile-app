package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ShoppingItem(
    val amount: Int,
    val idShoppingList: UUID,
    @PrimaryKey val idSpendingRecordData: UUID,
)
