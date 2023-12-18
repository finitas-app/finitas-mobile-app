package pl.finitas.app.sync_feature.domain.repository

import pl.finitas.app.core.data.model.MessagesVersion
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.sync_feature.data.data_source.SyncMessagesFromVersionResponse
import java.util.UUID

interface MessageSyncRepository {
    suspend fun upsertMessages(messages: List<RoomMessage>)
    suspend fun getMessagesByIdRoom(idRoom: UUID): List<RoomMessage>
    suspend fun getMessagesFromVersionRemote(versions: List<MessagesVersion>): SyncMessagesFromVersionResponse

    suspend fun getPendingMessages(): List<RoomMessage>
}