package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingList(
    val idUser: String?,
    @PrimaryKey val idShoppingList: String? = null,
)