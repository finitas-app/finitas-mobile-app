package pl.finitas.app.room_feature.data.data_sorce

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.RoomDao
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.room_feature.domain.repository.RoomRepository
import java.util.UUID

class RoomRepositoryImpl(
    private val roomDao: RoomDao,
): RoomRepository {
    override fun getRooms(): Flow<List<Room>> {
        return roomDao.getRooms()
    }

    override suspend fun getRoomById(idRoom: UUID): Room? {
        return roomDao.getRoomById(idRoom)
    }
}