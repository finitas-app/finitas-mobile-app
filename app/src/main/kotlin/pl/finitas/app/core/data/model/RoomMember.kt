package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomMember(
    val idRoom: String,
    val idRole: String,
    val idUser: String,
    @PrimaryKey val idRoomMember: String? = null,
)
