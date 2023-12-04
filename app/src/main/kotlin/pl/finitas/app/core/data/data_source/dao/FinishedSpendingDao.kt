package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.FinishedSpending

@Dao
interface FinishedSpendingDao {

    @Insert
    suspend fun insertFinishedSpending(finishedSpending: FinishedSpending): Long

    @Update
    suspend fun updateFinishedSpending(finishedSpending: FinishedSpending)

    @Delete
    suspend fun deleteFinishedSpending(finishedSpending: FinishedSpending)
}