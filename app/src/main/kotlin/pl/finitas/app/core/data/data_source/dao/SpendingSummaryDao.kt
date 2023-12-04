package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.User

@Dao
interface SpendingSummaryDao {

    @Insert
    suspend fun insertSpendingSummary(spendingSummary: SpendingSummary): Long

    @Update
    suspend fun updateSpendingSummary(spendingSummary: SpendingSummary)

    @Delete
    suspend fun deleteSpendingSummary(spendingSummary: SpendingSummary)
}