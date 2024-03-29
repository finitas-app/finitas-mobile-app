package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = User::class,
            parentColumns = ["idUser"],
            childColumns = ["idUser"],
        ),
    ],
    indices = [Index("idUser", "version")]
)
data class ShoppingList(
    @ColumnInfo(index = true)
    val idUser: UUID?,
    val color: Int,
    val name: String,
    @PrimaryKey val idShoppingList: UUID,
    val isFinished: Boolean,
    val isDeleted: Boolean,
    val version: Int?,
)
