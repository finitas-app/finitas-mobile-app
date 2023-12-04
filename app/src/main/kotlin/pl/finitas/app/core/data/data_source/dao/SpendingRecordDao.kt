package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.SpendingRecord

@Dao
interface SpendingRecordDao {

    @Insert
    suspend fun insertSpendingRecord(spendingRecord: SpendingRecord): Long

    @Update
    suspend fun updateSpendingRecord(spendingRecord: SpendingRecord)

    @Delete
    suspend fun deleteSpendingRecord(spendingRecord: SpendingRecord)
}