package pl.finitas.app.manage_spendings_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import java.util.UUID

interface TotalSpendingRepository {

    fun getFinishedSpendings(): Flow<List<FinishedSpendingWithRecordsDto>>

    suspend fun findFinishedSpendingWithRecordBy(idTotalSpending: UUID): FinishedSpendingWithRecordsDto?

    @Throws(TotalSpendingNotFoundException::class)
    suspend fun getFinishedSpendingWithRecordBy(idTotalSpending: UUID) =
        findFinishedSpendingWithRecordBy(idTotalSpending)
            ?: throw TotalSpendingNotFoundException(idTotalSpending)


    suspend fun upsertFinishedSpendingWithRecords(totalSpending: FinishedSpendingWithRecordsDto)

    suspend fun deleteFinishedSpending(totalSpending: FinishedSpending)
}


class TotalSpendingNotFoundException(idTotalSpending: UUID) :
    Exception("Total spending with id:$idTotalSpending not found!")