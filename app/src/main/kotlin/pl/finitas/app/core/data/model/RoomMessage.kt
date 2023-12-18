package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class RoomMessage(
    @PrimaryKey val idMessage: UUID,
    val idUser: UUID,
    val idRoom: UUID,
    val createdAt: LocalDateTime,
    val isPending: Boolean,
    val isRead: Boolean,
    val version: Int,
    val idShoppingList: UUID?,
    val content: String?,
)
