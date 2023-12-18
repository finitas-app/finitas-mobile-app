package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class RoomMember(
    val idRoom: UUID,
    val idRole: UUID?,
    @PrimaryKey val idRoomMember: UUID,
)
