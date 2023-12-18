package pl.finitas.app.sync_feature.domain.repository

import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.sync_feature.domain.RoomDto

interface RoomSyncRepository {

    suspend fun upsertRooms(rooms: List<RoomDto>)

    suspend fun getRoomsFromVersionRemote(versions: List<RoomVersion>): List<RoomDto>
}