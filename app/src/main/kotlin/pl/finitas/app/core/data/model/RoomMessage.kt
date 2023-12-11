package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class RoomMessage(
    val content: String?,
    val idShoppingList: UUID?,
    val createdAt: Int,
    val idRoomMember: UUID,
    @PrimaryKey val idMessage: UUID,
)
