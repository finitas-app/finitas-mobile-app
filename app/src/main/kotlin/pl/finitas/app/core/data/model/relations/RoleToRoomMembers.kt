package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.data.model.RoomRole

data class RoleToRoomMembers(
    @Embedded
    val roomRole: RoomRole,
    @Relation(
        parentColumn = "idRole",
        entityColumn = "idRole"
    )
    val roomMembers: List<RoomMember>,
)
