package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.RequestMethod

@Dao
interface RequestMethodDao {

    @Insert
    suspend fun insertRequestMethod(requestMethod: RequestMethod): Long

    @Update
    suspend fun updateRequestMethod(requestMethod: RequestMethod)

    @Delete
    suspend fun deleteRequestMethod(requestMethod: RequestMethod)
}