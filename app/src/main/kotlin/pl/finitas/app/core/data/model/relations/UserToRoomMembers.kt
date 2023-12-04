package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.data.model.User

data class UserToRoomMembers(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "idUser",
        entityColumn = "idUser"
    )
    val roomMembers: List<RoomMember>,
)
