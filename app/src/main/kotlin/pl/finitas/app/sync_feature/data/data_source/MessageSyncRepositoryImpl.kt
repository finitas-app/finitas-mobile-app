package pl.finitas.app.sync_feature.data.data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.data_source.dao.MessageDao
import pl.finitas.app.core.data.model.MessagesVersion
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.core.domain.dto.SerializableLocalDateTime
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.core.http.HttpUrls
import pl.finitas.app.sync_feature.domain.repository.MessageSyncRepository
import java.util.UUID

class MessageSyncRepositoryImpl(
    private val messageDao: MessageDao,
    private val httpClient: HttpClient,
): MessageSyncRepository {
    override suspend fun upsertMessages(messages: List<RoomMessage>) {
        messageDao.upsertMessages(messages)
    }

    override suspend fun getMessagesByIdRoom(idRoom: UUID): List<RoomMessage> {
        return messageDao.getMessagesByIdRoom(idRoom)
    }

    override suspend fun getMessagesFromVersionRemote(versions: List<MessagesVersion>): SyncMessagesFromVersionResponse {
        return httpClient.post(HttpUrls.syncMessages) {
            setBody(SyncMessagesFromVersionRequest(versions))
        }.body()
    }

    override suspend fun getPendingMessages(): List<RoomMessage> {
        return messageDao.getPendingMessages()
    }
}

@Serializable
data class SyncMessagesFromVersionRequest(
    val lastMessagesVersions: List<MessagesVersion>,
)

@Serializable
data class SyncMessagesFromVersionResponse(
    val messages: List<NewMessagesResponse>,
    val unavailableRooms: List<SerializableUUID>,
)

@Serializable
data class NewMessagesResponse(
    val idMessage: SerializableUUID,
    val idUser: SerializableUUID,
    val idRoom: SerializableUUID,
    val createdAt: SerializableLocalDateTime,
    val version: Int,
    val idShoppingList: SerializableUUID? = null,
    val content: String? = null,
)