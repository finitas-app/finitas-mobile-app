package pl.finitas.app.sync_feature.domain

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID

@Serializable
data class RoomDto(
    val idRoom: SerializableUUID,
    val name: String,
    val idInvitationLink: SerializableUUID,
    val version: Int,
    val roles: List<RoomRoleDto>,
    val members: List<RoomMemberDto>,
)

@Serializable
data class RoomRoleDto(
    val idRole: SerializableUUID,
    val name: String,
    val authorities: Set<Authority>,
)

@Serializable
data class RoomMemberDto(
    val idUser: SerializableUUID,
    val roomRole: RoomRoleDto? = null,
)

enum class Authority {
    READ_USERS_DATA,
    MODIFY_USERS_DATA,
    MODIFY_ROOM,
    MODIFY
}
