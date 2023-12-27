package pl.finitas.app.sync_feature.domain.repository

import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.sync_feature.domain.RoomDto
import java.util.UUID

interface RoomSyncRepository {

    suspend fun upsertRooms(rooms: List<RoomDto>)

    suspend fun getRoomsFromVersionRemote(versions: List<RoomVersion>): SyncRoomsResponse

    suspend fun deleteRooms(idsRoom: List<UUID>)
}

@Serializable
data class SyncRoomsResponse(
    val rooms: List<RoomDto>,
    val unavailableRooms: List<SerializableUUID>,
)
