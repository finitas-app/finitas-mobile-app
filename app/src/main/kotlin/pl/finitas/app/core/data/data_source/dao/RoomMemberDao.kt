package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.data.model.RoomMember
import java.util.UUID

@Dao
interface RoomMemberDao {

    @Query("""
        SELECT rl.authorities as 'authorities'
        FROM RoomMember rm
        JOIN RoomRole rl ON rm.idRole = rl.idRole
        WHERE rm.idUser == :idUser AND rm.idRoom = :idRoom
    """)
    fun getAuthorityBy(idUser: UUID?, idRoom: UUID): Flow<AuthoritiesDto?>

    @Insert
    suspend fun insertRoomMember(roomMember: RoomMember): Long

    @Update
    suspend fun updateRoomMember(roomMember: RoomMember)

    @Delete
    suspend fun deleteRoomMember(roomMember: RoomMember)
}

data class AuthoritiesDto(
    val authorities: Set<Authority>,
)