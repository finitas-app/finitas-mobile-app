package pl.finitas.app.room_feature.domain

import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.room_feature.presentation.room_settings.roles.UpsertRoleState
import java.time.LocalDateTime
import java.util.UUID

sealed interface ChatMessage {
    val idMessage: UUID
    val time: LocalDateTime
}

sealed interface IncomingChatMessage: ChatMessage {
    val isRead: Boolean
    val sender: String?

    fun clearSender(): IncomingChatMessage
}

data class IncomingTextMessage(
    override val time: LocalDateTime,
    override val idMessage: UUID,
    override val isRead: Boolean,
    override val sender: String?,
    override val message: String,
) : IncomingChatMessage, TextMessage {
    override fun clearSender() = copy(sender = null)
}

data class IncomingShoppingListMessage(
    override val time: LocalDateTime,
    override val idMessage: UUID,
    override val isRead: Boolean,
    override val sender: String?,
    override val idShoppingList: UUID,
) : IncomingChatMessage, ShoppingListMessage {
    override fun clearSender() = copy(sender = null)
}

sealed interface OutgoingChatMessage: ChatMessage {
    val isPending: Boolean
}
sealed interface TextMessage {
    val message: String
}

sealed interface ShoppingListMessage {
    val idShoppingList: UUID
}

data class OutgoingTextMessage(
    override val time: LocalDateTime,
    override val isPending: Boolean,
    override val idMessage: UUID,
    override val message: String,
) : OutgoingChatMessage, TextMessage

data class OutgoingShoppingListMessage(
    override val time: LocalDateTime,
    override val isPending: Boolean,
    override val idMessage: UUID,
    override val idShoppingList: UUID,
) : OutgoingChatMessage, ShoppingListMessage

data class RoomPreviewDto(
    val idRoom: UUID,
    val title: String,
    val lastMessage: String,
    val unreadMessagesNumber: Int,
)

@Serializable
data class CreateRoomDto(
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


