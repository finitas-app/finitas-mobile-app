package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
    suspend fun getRoomById(idRoom: UUID): Room


    @Transaction
    suspend fun upsertRoomsWithMembersAndRoles(rooms: List<RoomDto>) {
        val roomMembers = rooms.flatMap { room ->
            val members = room.members.map {
                RoomMember(
                    idRoom = room.idRoom,
                    idRole = it.idRole,
                    idUser = it.idUser,
                    isActive = true,
                )
            }
            deactivateUnavailableRoomMembers(room.idRoom, members.map { it.idRoom })
            members
        }
        deleteRoles(rooms.map { it.idRoom })
        upsertRooms(rooms.map { Room(it.idRoom, it.name, it.idInvitationLink) })
        upsertRoomRoles(rooms.flatMap { room ->
            room.roles.map {
                RoomRole(
                    it.name,
                    room.idRoom,
                    it.authorities,
                    it.idRole,
                )
            }
        })
        insertUsers(rooms.flatMap { room ->
            room.members.map {
                User(
                    idUser = it.idUser,
                    username = "",
                )
            }
        })
        upsertRoomMembers(roomMembers)
    }



    @Upsert
    suspend fun upsertRooms(rooms: List<Room>)

    @Upsert
    suspend fun upsertRoomRoles(roomRoles: List<RoomRole>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<User>)

    @Upsert
    suspend fun upsertRoomMembers(roomMembers: List<RoomMember>)

    @Query("SELECT * FROM Room WHERE idRoom = :idRoom")
    fun getRoomByIdFlow(idRoom: UUID): Flow<Room>

    @Query(
        """
        SELECT 
            rm.idUser as "idUser",
            u.username as "username",
            rm.idRole as "idRole"
        FROM RoomMember rm
        JOIN User u on u.idUser = rm.idUser
        WHERE rm.idRoom = :idRoom and isActive = 1
    """
    )
    fun getRoomMembers(idRoom: UUID): Flow<List<RoomMemberFlatDto>>

    @Query("SELECT * FROM RoomRole WHERE idRoom = :idRoom")
    fun getRoomRolesByIdRoom(idRoom: UUID): Flow<List<RoomRole>>

    @Query("DELETE FROM Room WHERE idRoom in (:idsRoom)")
    suspend fun deleteRooms(idsRoom: List<UUID>)

    @Query("""
        UPDATE RoomMember
        SET idRole = null, isActive = 0
        WHERE idRoom = :idRoom and idUser not in (:availableUsers)
    """)
    suspend fun deactivateUnavailableRoomMembers(idRoom: UUID, availableUsers: List<UUID>)

    @Query("DELETE FROM RoomRole WHERE idRoom in (:idsRoom)")
    suspend fun deleteRoles(idsRoom: List<UUID>)

    @Query("DELETE FROM Room WHERE 1 = 1")
    suspend fun deleteAllRooms()
}

data class RoomMemberFlatDto(
    val idUser: UUID,
    val username: String,
    val idRole: UUID?,
)
