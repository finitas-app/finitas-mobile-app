package pl.finitas.app.core.domain.repository

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.core.domain.dto.store.DeleteFinishedSpendingRequest
import pl.finitas.app.core.domain.dto.store.FinishedSpendingDto

interface FinishedSpendingStoreRepository {

    suspend fun changeFinishedSpendings(request: List<FinishedSpendingDto>)
    suspend fun synchronizeFinishedSpendings(request: List<FinishedSpendingVersion>): List<FinishedSpendingSyncDto>
    suspend fun getAllFinishedSpendings(idUser: String): List<FinishedSpendingDto>
    suspend fun createFinishedSpending(dto: FinishedSpendingDto)
    suspend fun updateFinishedSpending(dto: FinishedSpendingDto)
    suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest)
}

@Serializable
data class FinishedSpendingSyncDto(
    val idUser: SerializableUUID,
    val actualVersion: Int,
    val finishedSpendings: List<FinishedSpendingDto>,
)

@Serializable
data class FinishedSpendingVersion(
    val idUser: SerializableUUID,
    val version: Int,
)
