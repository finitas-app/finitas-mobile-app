package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    val content: String?,
    val idShoppingList: String?,
    val createdAt: Int,
    val idRoomMember: String,
    @PrimaryKey val idMessage: String? = null,
)
