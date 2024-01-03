package pl.finitas.app.sync_feature.data.data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.data_source.dao.RoomDao
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.core.data.model.User
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
        return httpClient.post(HttpUrls.syncRooms) {
            setBody(SyncRoomsFromVersionsRequest(versions))
        }.body()
    }

    override suspend fun deleteRooms(idsRoom: List<UUID>) {
        roomDao.deleteRooms(idsRoom)
    }

    override suspend fun getRoomsWithAuthority(authorizedUserId: UUID, authority: Authority): List<UUID> {
        return roomDao.getRoomsWithAuthority(authorizedUserId, authority).map { it.idRoom }
    }

    override suspend fun getRoomMembers(idsRoom: List<UUID>): List<User> {
        return roomDao.getRoomMembers(idsRoom)
    }
}

@Serializable
data class SyncRoomsFromVersionsRequest(
    val roomVersions: List<RoomVersion>,
)
