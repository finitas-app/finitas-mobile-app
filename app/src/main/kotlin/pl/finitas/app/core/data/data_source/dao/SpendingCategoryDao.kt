package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.SpendingCategory
import java.util.UUID

@Dao
interface SpendingCategoryDao {


    @Query("SELECT * FROM SpendingCategory ORDER BY createdAt")
    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    @Query("SELECT * FROM SpendingCategory")
    suspend fun getSpendingCategories(): List<SpendingCategory>

    @Query("SELECT * FROM SpendingCategory WHERE idCategory = :idCategory")
    suspend fun findSpendingCategoryBy(idCategory: UUID): SpendingCategory?

    @Insert
    suspend fun insertSpendingCategory(spendingCategory: SpendingCategory): Long

    @Update
    suspend fun updateSpendingCategory(spendingCategory: SpendingCategory)

    @Upsert
    suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory)

    @Delete
    suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory)
}