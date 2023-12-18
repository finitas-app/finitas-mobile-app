package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import pl.finitas.app.core.data.model.MessagesVersion
import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.sync_feature.domain.repository.VersionsRepository
import java.util.UUID

@Dao
interface VersionsDao: VersionsRepository {

    @Query("SELECT * FROM RoomVersion")
    override suspend fun getRoomVersions(): List<RoomVersion>

    @Query("SELECT * FROM RoomVersion WHERE idRoom = :idRoom")
    override suspend fun getRoomVersion(idRoom: UUID): RoomVersion

    @Upsert
    override suspend fun setRoomVersion(roomVersion: RoomVersion)

    @Upsert
    override suspend fun setRoomVersions(roomVersions: List<RoomVersion>)

    @Query("DELETE FROM RoomVersion WHERE idRoom = :idRoom")
    override suspend fun deleteRoomVersion(idRoom: UUID)


    @Query("SELECT * FROM MessagesVersion")
    override suspend fun getMessagesVersions(): List<MessagesVersion>

    @Query("SELECT * FROM MessagesVersion WHERE idRoom = :idRoom")
    override suspend fun getMessagesVersion(idRoom: UUID): MessagesVersion

    @Upsert
    override suspend fun setMessagesVersion(messagesVersion: MessagesVersion)

    @Upsert
    override suspend fun setMessagesVersions(messagesVersions: List<MessagesVersion>)

    @Query("DELETE FROM MessagesVersion WHERE idRoom = :idRoom")
    override suspend fun deleteMessagesVersion(idRoom: UUID)
}