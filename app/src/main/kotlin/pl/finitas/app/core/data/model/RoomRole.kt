package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class RoomRole(
    val name: String,
    val idRoom: UUID,
    @PrimaryKey val idRole: UUID,
)
