package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingList(
    val idMessageLine: Int?,
    val idUser: Int?,
    @PrimaryKey val idShoppingList: Int? = null,
)
