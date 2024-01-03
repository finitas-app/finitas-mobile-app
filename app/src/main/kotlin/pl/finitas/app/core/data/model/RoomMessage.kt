package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Room::class,
            parentColumns = ["idRoom"],
            childColumns = ["idRoom"]
        ),
    ],
    indices = [
        Index("idUser", "idRoom")
    ]
)
data class RoomMessage(
    @PrimaryKey val idMessage: UUID,
    @ColumnInfo(index = true)
    val idUser: UUID,
    @ColumnInfo(index = true)
    val idRoom: UUID,
    val createdAt: LocalDateTime,
    val isPending: Boolean,
    val isRead: Boolean,
    val version: Int,
    val idShoppingList: UUID?,
    val content: String?,
)
