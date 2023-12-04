package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingItem(
    val isDone: Int,
    val idShoppingList: String,
    @PrimaryKey val idSpendingRecordData: String? = null,
)
