package pl.finitas.app.core.domain.repository

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.core.domain.dto.store.DeleteFinishedSpendingRequest
import pl.finitas.app.core.domain.dto.store.FetchUpdatesResponse
import pl.finitas.app.core.domain.dto.store.RemoteFinishedSpendingDto

interface FinishedSpendingStoreRepository {

    suspend fun changeFinishedSpendings(request: List<RemoteFinishedSpendingDto>)
    suspend fun synchronizeFinishedSpendings(request: List<FinishedSpendingVersion>): List<FetchUpdatesResponse<RemoteFinishedSpendingDto>>
    suspend fun getAllFinishedSpendings(idUser: String): List<RemoteFinishedSpendingDto>
    suspend fun createFinishedSpending(dto: RemoteFinishedSpendingDto)
    suspend fun updateFinishedSpending(dto: RemoteFinishedSpendingDto)
    suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest)
}

@Serializable
data class FinishedSpendingVersion(
    val idUser: SerializableUUID,
    val version: Int,
)
