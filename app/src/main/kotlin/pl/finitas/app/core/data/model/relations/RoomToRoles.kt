package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.data.model.Role

data class RoomToRoles(
    @Embedded
    val room: Room,
    @Relation(
        parentColumn = "idRoom",
        entityColumn = "idRoom"
    )
    val roles: List<Role>,
)
