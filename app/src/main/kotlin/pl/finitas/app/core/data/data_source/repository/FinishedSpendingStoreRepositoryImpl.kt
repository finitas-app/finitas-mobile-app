package pl.finitas.app.core.data.data_source.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import pl.finitas.app.core.domain.dto.store.DeleteFinishedSpendingRequest
import pl.finitas.app.core.domain.dto.store.FinishedSpendingDto
import pl.finitas.app.core.domain.dto.store.SynchronizationRequest
import pl.finitas.app.core.domain.dto.store.SynchronizationResponse
import pl.finitas.app.core.domain.repository.FinishedSpendingStoreRepository
import pl.finitas.app.core.http.HttpUrls

class FinishedSpendingStoreRepositoryImpl(private val httpClient: HttpClient) :
    FinishedSpendingStoreRepository {
    override suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>): SynchronizationResponse<FinishedSpendingDto> {
        return httpClient.put("${HttpUrls.finishedSpendingsStore}/synchronize") {
            setBody(request)
        }.body()
    }

    override suspend fun getAllFinishedSpendings(idUser: String): List<FinishedSpendingDto> {
        return httpClient.get("${HttpUrls.finishedSpendingsStore}/$idUser").body()
    }

    override suspend fun createFinishedSpending(dto: FinishedSpendingDto) {
        httpClient.post(HttpUrls.finishedSpendingsStore) {
            setBody(dto)
        }
    }

    override suspend fun updateFinishedSpending(dto: FinishedSpendingDto) {
        httpClient.patch(HttpUrls.finishedSpendingsStore) {
            setBody(dto)
        }
    }

    override suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest) {
        httpClient.delete(HttpUrls.finishedSpendingsStore) {
            setBody(request)
        }
    }
}