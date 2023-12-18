package pl.finitas.app.room_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Room
import java.util.UUID

interface RoomRepository {
    fun getRooms(): Flow<List<Room>>
    suspend fun getRoomById(idRoom: UUID): Room?
}