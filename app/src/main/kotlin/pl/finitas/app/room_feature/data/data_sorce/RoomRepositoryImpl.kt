package pl.finitas.app.room_feature.data.data_sorce

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.RoomDao
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.http.HttpUrls
import pl.finitas.app.room_feature.domain.AddRoomDto
import pl.finitas.app.room_feature.domain.repository.RoomRepository
import pl.finitas.app.sync_feature.domain.RoomDto
import java.util.UUID

class RoomRepositoryImpl(
    private val roomDao: RoomDao,
    private val httpClient: HttpClient,
): RoomRepository {
    override fun getRooms(): Flow<List<Room>> {
        return roomDao.getRooms()
    }

    override suspend fun getRoomById(idRoom: UUID): Room? {
        return roomDao.getRoomById(idRoom)
    }

    override suspend fun addRoomRepository(addRoomDto: AddRoomDto) {
        val response: RoomDto = httpClient.post(HttpUrls.addRoom) {
            setBody(addRoomDto)
        }.body()
        roomDao.upsertRoomsWithMembersAndRoles(listOf(response))
    }
}
