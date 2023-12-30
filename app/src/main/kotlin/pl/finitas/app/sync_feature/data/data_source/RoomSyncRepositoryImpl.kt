package pl.finitas.app.sync_feature.data.data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pl.finitas.app.core.data.data_source.dao.RoomDao
import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.core.http.HttpUrls
import pl.finitas.app.sync_feature.domain.RoomDto
import pl.finitas.app.sync_feature.domain.repository.RoomSyncRepository
import pl.finitas.app.sync_feature.domain.repository.SyncRoomsResponse
import java.util.UUID

class RoomSyncRepositoryImpl(
    private val roomDao: RoomDao,
    private val httpClient: HttpClient,
): RoomSyncRepository {

    override suspend fun upsertRooms(rooms: List<RoomDto>) {
        roomDao.upsertRoomsWithMembersAndRoles(rooms)
    }

    override suspend fun getRoomsFromVersionRemote(versions: List<RoomVersion>): SyncRoomsResponse {
        val response: String = httpClient.post(HttpUrls.syncRooms) {
            setBody(SyncRoomsFromVersionsRequest(versions))
        }.body()
        return Json.decodeFromString(response)
    }

    override suspend fun deleteRooms(idsRoom: List<UUID>) {
        roomDao.deleteRooms(idsRoom)
    }
}

@Serializable
data class SyncRoomsFromVersionsRequest(
    val roomVersions: List<RoomVersion>,
)
