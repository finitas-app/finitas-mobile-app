package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.SpendingRecordData

@Dao
interface SpendingRecordDataDao {

    @Insert
    suspend fun insertSpendingRecordData(spendingRecordData: SpendingRecordData): Long

    @Update
    suspend fun updateSpendingRecordData(spendingRecordData: SpendingRecordData)

    @Delete
    suspend fun deleteSpendingRecordData(spendingRecordData: SpendingRecordData)
}