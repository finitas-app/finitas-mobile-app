package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.data.model.Role

data class RoleToRoomMembers(
    @Embedded
    val role: Role,
    @Relation(
        parentColumn = "idRole",
        entityColumn = "idRole"
    )
    val roomMembers: List<RoomMember>,
)
