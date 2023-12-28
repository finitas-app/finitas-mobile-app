package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = User::class,
            parentColumns = ["idUser"],
            childColumns = ["idUser"]
        ),
    ],
)
data class ShoppingList(
    @ColumnInfo(index = true)
    val idUser: UUID?,
    val color: Int,
    val name: String,
    @PrimaryKey val idShoppingList: UUID,
)
