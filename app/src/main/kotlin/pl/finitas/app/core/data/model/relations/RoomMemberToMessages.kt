package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.data.model.Message

data class RoomMemberToMessages(
    @Embedded
    val roomMember: RoomMember,
    @Relation(
        parentColumn = "idRoomMember",
        entityColumn = "idRoomMember"
    )
    val messages: List<Message>,
)
