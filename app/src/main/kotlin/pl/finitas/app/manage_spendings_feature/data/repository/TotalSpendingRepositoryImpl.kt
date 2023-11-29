package pl.finitas.app.manage_spendings_feature.data.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.data.data_source.TotalSpendingDao
import pl.finitas.app.manage_spendings_feature.domain.model.TotalSpending
import pl.finitas.app.manage_spendings_feature.domain.model.relations.TotalSpendingWithRecords
import pl.finitas.app.manage_spendings_feature.domain.repository.TotalSpendingRepository

class TotalSpendingRepositoryImpl(
    private val dao: TotalSpendingDao,
): TotalSpendingRepository {

    override fun getTotalSpendings(): Flow<List<TotalSpendingWithRecords>> {
        return dao.getTotalSpendings()
    }

    override suspend fun findTotalSpendingBy(idTotalSpending: Int): TotalSpending? {
        return dao.findTotalSpendingBy(idTotalSpending)
    }

    override suspend fun upsertTotalSpendingWithRecords(totalSpendingWithRecords: TotalSpendingWithRecords) {
        val (totalSpending, spendingRecords) = totalSpendingWithRecords
        dao.upsertTotalSpendingWithRecords(totalSpending, spendingRecords)
    }

    override suspend fun deleteTotalSpending(totalSpending: TotalSpending) {
        dao.deleteTotalSpending(totalSpending)
    }
}