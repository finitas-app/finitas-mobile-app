package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ShoppingList(
    val idUser: UUID?,
    val color: Int,
    val name: String,
    @PrimaryKey val idShoppingList: UUID,
)
