package pl.finitas.app.manage_spendings_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.domain.model.TotalSpending
import pl.finitas.app.manage_spendings_feature.domain.model.relations.TotalSpendingWithRecords

interface TotalSpendingRepository {

    fun getTotalSpendings(): Flow<List<TotalSpendingWithRecords>>

    suspend fun findTotalSpendingBy(idTotalSpending: Int): TotalSpending?

    @Throws(TotalSpendingNotFoundException::class)
    suspend fun getTotalSpendingBy(idTotalSpending: Int) =
        findTotalSpendingBy(idTotalSpending)
            ?: throw TotalSpendingNotFoundException(idTotalSpending)


    suspend fun upsertTotalSpending(totalSpending: TotalSpending)

    suspend fun deleteTotalSpending(totalSpending: TotalSpending)
}


class TotalSpendingNotFoundException(idTotalSpending: Int) :
    Exception("Total spending with id:$idTotalSpending not found!")