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
            entity = ShoppingList::class,
            parentColumns = ["idShoppingList"],
            childColumns = ["idShoppingList"]
        ),
    ],
)
data class ShoppingItem(
    val amount: Int,
    @ColumnInfo(index = true)
    val idShoppingList: UUID,
    @PrimaryKey val idSpendingRecordData: UUID,
)
