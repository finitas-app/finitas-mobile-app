package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.DelayedRequest

@Dao
interface DelayedRequestDao {

    @Insert
    suspend fun insertDelayedRequest(delayedRequest: DelayedRequest): Long

    @Update
    suspend fun updateDelayedRequest(delayedRequest: DelayedRequest)

    @Delete
    suspend fun deleteDelayedRequest(delayedRequest: DelayedRequest)
}