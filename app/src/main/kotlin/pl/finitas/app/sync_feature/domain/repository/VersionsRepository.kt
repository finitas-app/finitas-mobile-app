package pl.finitas.app.sync_feature.domain.repository

import pl.finitas.app.core.data.model.MessagesVersion
import pl.finitas.app.core.data.model.RoomVersion
import java.util.UUID

interface VersionsRepository {

    suspend fun getRoomVersions(): List<RoomVersion>

    suspend fun getRoomVersion(idRoom: UUID): RoomVersion

    suspend fun setRoomVersion(roomVersion: RoomVersion)

    suspend fun setRoomVersions(roomVersions: List<RoomVersion>)

    suspend fun deleteRoomVersion(idRoom: UUID)

    suspend fun getMessagesVersions(): List<MessagesVersion>

    suspend fun getMessagesVersion(idRoom: UUID): MessagesVersion

    suspend fun setMessagesVersion(messagesVersion: MessagesVersion)

    suspend fun setMessagesVersions(messagesVersions: List<MessagesVersion>)

    suspend fun deleteMessagesVersion(idRoom: UUID)
}