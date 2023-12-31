package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID

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
@Serializable
data class SpendingCategory(
    val name: String,
    val idParent: SerializableUUID?,
    @ColumnInfo(index = true)
    val idUser: SerializableUUID? = null,
    @PrimaryKey val idCategory: SerializableUUID,
    val version: Int?,
    val isDeleted: Boolean,
)
