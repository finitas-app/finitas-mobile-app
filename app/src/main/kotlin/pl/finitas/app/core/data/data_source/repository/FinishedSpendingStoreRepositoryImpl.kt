package pl.finitas.app.core.data.data_source.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pl.finitas.app.core.domain.dto.store.DeleteFinishedSpendingRequest
import pl.finitas.app.core.domain.dto.store.FetchUpdatesResponse
import pl.finitas.app.core.domain.dto.store.FinishedSpendingDto
import pl.finitas.app.core.domain.repository.FinishedSpendingStoreRepository
import pl.finitas.app.core.domain.repository.FinishedSpendingVersion
import pl.finitas.app.core.http.HttpUrls

class FinishedSpendingStoreRepositoryImpl(private val httpClient: HttpClient) :
    FinishedSpendingStoreRepository {
    override suspend fun changeFinishedSpendings(request: List<FinishedSpendingDto>) {
        // TODO: change url and method
        httpClient.put("${HttpUrls.finishedSpendingsStore}/sync") {
            setBody(request)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun synchronizeFinishedSpendings(request: List<FinishedSpendingVersion>): List<FetchUpdatesResponse<FinishedSpendingDto>> {
        // TODO: change url and method
        return httpClient.post("${HttpUrls.finishedSpendingsStore}/sync") {
            setBody(request)
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getAllFinishedSpendings(idUser: String): List<FinishedSpendingDto> {
        return httpClient.get("${HttpUrls.finishedSpendingsStore}/$idUser"){
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun createFinishedSpending(dto: FinishedSpendingDto) {
        httpClient.post(HttpUrls.finishedSpendingsStore) {
            setBody(dto)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun updateFinishedSpending(dto: FinishedSpendingDto) {
        httpClient.patch(HttpUrls.finishedSpendingsStore) {
            setBody(dto)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest) {
        httpClient.delete(HttpUrls.finishedSpendingsStore) {
            setBody(request)
            contentType(ContentType.Application.Json)
        }
    }
}