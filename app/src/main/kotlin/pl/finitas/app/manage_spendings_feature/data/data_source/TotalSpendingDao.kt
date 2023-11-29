package pl.finitas.app.manage_spendings_feature.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecord
import pl.finitas.app.manage_spendings_feature.domain.model.TotalSpending
import pl.finitas.app.manage_spendings_feature.domain.model.relations.TotalSpendingWithRecords

@Dao
interface TotalSpendingDao {

    @Transaction
    @Query("SELECT * FROM TotalSpending")
    fun getTotalSpendings(): Flow<List<TotalSpendingWithRecords>>

    @Query("SELECT * FROM TotalSpending WHERE idTotalSpending = :idTotalSpending")
    suspend fun findTotalSpendingBy(idTotalSpending: Int): TotalSpending?

    @Insert
    suspend fun insertTotalSpending(totalSpending: TotalSpending): Long

    @Update
    suspend fun updateTotalSpending(totalSpending: TotalSpending)


    @Upsert
    suspend fun upsertTotalSpendingRecords(spendingRecords: List<SpendingRecord>)

    @Transaction
    suspend fun upsertTotalSpendingWithRecords(totalSpending: TotalSpending, spendingRecords: List<SpendingRecord>) {
        if (totalSpending.idTotalSpending == null) {
            val generatedId = insertTotalSpending(totalSpending)
            upsertTotalSpendingRecords(spendingRecords.map { it.copy(idSpendingRecord = null, idTotalSpending = generatedId.toInt()) })
        } else {
            updateTotalSpending(totalSpending)
            upsertTotalSpendingRecords(spendingRecords.map { it.copy(idTotalSpending = totalSpending.idTotalSpending) })
        }
    }

    @Delete
    suspend fun deleteTotalSpending(totalSpending: TotalSpending)
}