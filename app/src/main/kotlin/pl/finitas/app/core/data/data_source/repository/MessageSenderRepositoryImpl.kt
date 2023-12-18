package pl.finitas.app.core.data.data_source.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import pl.finitas.app.core.domain.repository.MessageSenderRepository
import pl.finitas.app.core.domain.repository.SendMessageRequest
import pl.finitas.app.core.http.HttpUrls

class MessageSenderRepositoryImpl(
    private val httpClient: HttpClient,
): MessageSenderRepository {
    override suspend fun sendMessages(sendMessageRequest: SendMessageRequest) {
        httpClient.post(HttpUrls.sendMessage) {
            setBody(sendMessageRequest)
        }
    }
}