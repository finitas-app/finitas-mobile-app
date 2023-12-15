package pl.finitas.app.core.domain.repository

import pl.finitas.app.core.domain.dto.store.DeleteFinishedSpendingRequest
import pl.finitas.app.core.domain.dto.store.FinishedSpendingDto
import pl.finitas.app.core.domain.dto.store.SynchronizationRequest
import pl.finitas.app.core.domain.dto.store.SynchronizationResponse

interface FinishedSpendingStoreRepository {
    suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>): SynchronizationResponse<FinishedSpendingDto>
    suspend fun getAllFinishedSpendings(idUser: String): List<FinishedSpendingDto>
    suspend fun createFinishedSpending(dto: FinishedSpendingDto)
    suspend fun updateFinishedSpending(dto: FinishedSpendingDto)
    suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest)
}