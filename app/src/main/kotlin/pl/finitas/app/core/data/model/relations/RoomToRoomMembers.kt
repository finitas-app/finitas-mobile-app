package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.data.model.RoomMember

data class RoomToRoomMembers(
    @Embedded
    val room: Room,
    @Relation(
        parentColumn = "idRoom",
        entityColumn = "idRoom"
    )
    val roomMembers: List<RoomMember>,
)
