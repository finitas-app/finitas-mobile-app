package pl.finitas.app.room_feature.domain

import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.room_feature.presentation.room_settings.roles.UpsertRoleState
import java.time.LocalDateTime
import java.util.UUID

sealed interface ChatMessage {
    val idMessage: UUID
    val message: String
    val time: LocalDateTime
}

sealed interface IncomingChatMessage: ChatMessage

data class IncomingTextMessage(
    override val message: String,
    override val time: LocalDateTime,
    override val idMessage: UUID,
    val isRead: Boolean,
    val sender: String?,
) : IncomingChatMessage

sealed interface OutgoingChatMessage: ChatMessage {
    val isPending: Boolean
}

data class OutgoingTextMessage(
    override val message: String,
    override val time: LocalDateTime,
    override val isPending: Boolean,
    override val idMessage: UUID,
) : OutgoingChatMessage

data class RoomPreviewDto(
    val idRoom: UUID,
    val title: String,
    val lastMessage: String,
    val unreadMessagesNumber: Int,
)

@Serializable
data class AddRoomDto(
    val roomName: String,
)

data class RoomWithAdditionalInfoView(
    val idRoom: UUID,
    val title: String,
    val invitationLinkUUID: UUID,
    val roomRoles: List<RoomRoleView>,
    val roomMembers: List<RoomMemberView>,
) {
    companion object {
        val empty get() = RoomWithAdditionalInfoView(
            idRoom = UUID.randomUUID(),
            title = "",
            invitationLinkUUID = UUID.randomUUID(),
            roomMembers = listOf(),
            roomRoles = listOf(),
        )
    }
}

data class RoomMemberView(
    val idUser: UUID,
    val username: String,
    val roomRole: RoomRoleView?,
)

data class RoomRoleView(
    val idRole: UUID,
    val name: String,
    val authorities: Set<Authority>,
) {
    fun toState() = UpsertRoleState(
        idRole = idRole,
        name = name,
        authorities = authorities,
    )
}


