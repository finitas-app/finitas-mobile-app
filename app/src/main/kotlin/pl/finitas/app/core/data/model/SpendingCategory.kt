package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = CASCADE,
            entity = User::class,
            parentColumns = ["idUser"],
            childColumns = ["idUser"]
        ),
    ],
)
data class SpendingCategory(
    val name: String,
    val idParent: UUID?,
    @ColumnInfo(index = true)
    val idUser: UUID? = null,
    @PrimaryKey val idCategory: UUID,
)
