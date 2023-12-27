package pl.finitas.app.room_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.room_feature.domain.AddRoomDto
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import java.util.UUID

interface RoomRepository {
    fun getRooms(): Flow<List<Room>>

    suspend fun getRoomById(idRoom: UUID): Room

    suspend fun addRoomRepository(addRoomDto: AddRoomDto)

    fun getRoomWithAdditionalInfo(idRoom: UUID): Flow<RoomWithAdditionalInfoView>

    suspend fun addRole(roomRole: AddRoleRequest)

    suspend fun updateRole(roomRole: UpdateRoleRequest)

    suspend fun deleteRole(roomRole: DeleteRoleRequest)
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
