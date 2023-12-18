package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.data.model.RoomRole
import pl.finitas.app.core.data.model.User
import pl.finitas.app.sync_feature.domain.RoomDto
import java.util.UUID

@Dao
interface RoomDao {

    @Query("SELECT * FROM Room")
    fun getRooms(): Flow<List<Room>>

    @Query("SELECT * FROM Room WHERE idRoom = :idRoom")
    suspend fun getRoomById(idRoom: UUID): Room?


    @Transaction
    suspend fun upsertRoomsWithMembersAndRoles(rooms: List<RoomDto>) {
        upsertRooms(rooms.map { Room(it.idRoom, it.name) })
        upsertRoomRoles(rooms.flatMap { room ->
            room.roles.map {
                RoomRole(
                    it.name,
                    room.idRoom,
                    it.idRole,
                )
            }
        })
        upsertUsers(rooms.flatMap { room ->
            room.members.map {
                User(
                    idUser = it.idUser,
                    username = "",
                )
            }
        })
        upsertRoomMembers(rooms.flatMap { room ->
            room.members.map {
                RoomMember(
                    idRoom = room.idRoom,
                    idRole = it.roomRole?.idRole,
                    idRoomMember = it.idUser,
                )
            }
        })
    }

    @Upsert
    suspend fun upsertRooms(rooms: List<Room>)

    @Upsert
    suspend fun upsertRoomRoles(roomRoles: List<RoomRole>)

    @Upsert
    suspend fun upsertUsers(users: List<User>)

    @Upsert
    suspend fun upsertRoomMembers(roomMembers: List<RoomMember>)
}
