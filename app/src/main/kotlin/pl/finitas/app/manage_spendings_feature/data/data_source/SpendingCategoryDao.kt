package pl.finitas.app.manage_spendings_feature.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingCategory

@Dao
interface SpendingCategoryDao {

    @Query("SELECT * FROM SpendingCategory ORDER BY createdAt")
    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    @Query("SELECT * FROM SpendingCategory")
    suspend fun getSpendingCategories(): List<SpendingCategory>

    @Query("SELECT * FROM SpendingCategory WHERE idCategory = :idCategory")
    suspend fun findSpendingCategoryBy(idCategory: Int): SpendingCategory?

    @Upsert
    suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory)

    @Delete
    suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory)
}