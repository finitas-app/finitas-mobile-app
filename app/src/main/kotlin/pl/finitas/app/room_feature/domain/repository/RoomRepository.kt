package pl.finitas.app.room_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.room_feature.domain.CreateRoomDto
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import java.util.UUID

interface RoomRepository {
    fun getRooms(): Flow<List<Room>>

    fun findRoomById(idRoom: UUID): Flow<Room?>

    suspend fun createRoom(createRoomDto: CreateRoomDto)

    fun getRoomWithAdditionalInfo(idRoom: UUID): Flow<RoomWithAdditionalInfoView>

    suspend fun addRole(roomRole: AddRoleRequest)

    suspend fun updateRole(roomRole: UpdateRoleRequest)

    suspend fun deleteRole(roomRole: DeleteRoleRequest)

    suspend fun deleteUserFromRoom(deleteUserRequest: DeleteUserRequest)

    suspend fun assignRoleToUser(assignRoleToUserRequest: AssignRoleToUserRequest)

    fun getAuthorizedUserAuthorities(idUser: UUID?, idRoom: UUID): Flow<Set<Authority>>
    suspend fun regenerateLink(idRoom: UUID)
    suspend fun changeRoomName(idRoom: UUID, newName: String)
    suspend fun getRoomMembers(idRoom: UUID): List<RoomMember>
    suspend fun joinRoom(joinRoomDto: JoinRoomDto)
}

@Serializable
data class DeleteRoleRequest(
    val idRoom: SerializableUUID,
    val idRole: SerializableUUID,
)

@Serializable
data class UpdateRoleRequest(
    val idRoom: SerializableUUID,
    val idRole: SerializableUUID,
    val name: String,
    val authorities: Set<Authority>,
)

@Serializable
data class AddRoleRequest(
    val idRoom: SerializableUUID,
    val name: String,
    val authorities: Set<Authority>,
)

@Serializable
data class DeleteUserRequest(
    val idRoom: SerializableUUID,
    val idUser: SerializableUUID,
)

@Serializable
data class AssignRoleToUserRequest(
    val idRoom: SerializableUUID,
    val idRole: SerializableUUID?,
    val idUser: SerializableUUID,
)



@Serializable
data class JoinRoomDto(
    val idInvitationLink: SerializableUUID,
)
