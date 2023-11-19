package pl.finitas.app.manage_spendings_feature.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.domain.model.TotalSpending
import pl.finitas.app.manage_spendings_feature.domain.model.relations.TotalSpendingWithRecords

@Dao
interface TotalSpendingDao {

    @Query("SELECT * FROM TotalSpending")
    fun getTotalSpendings(): Flow<List<TotalSpendingWithRecords>>

    @Query("SELECT * FROM TotalSpending WHERE idTotalSpending = :idTotalSpending")
    suspend fun findTotalSpendingBy(idTotalSpending: Int): TotalSpending?

    @Upsert
    suspend fun upsertTotalSpending(totalSpending: TotalSpending)

    @Delete
    suspend fun deleteTotalSpending(totalSpending: TotalSpending)
}