package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Category

@Dao
interface CategoryDao {

    @Transaction
    @Query("SELECT * FROM Category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE idCategory = :idCategory")
    suspend fun findCategoryBy(idCategory: Int): Category?

    @Query("SELECT * FROM Category WHERE idParent = :idParent")
    suspend fun findChildrenCategoryBy(idParent: Int): List<Category>?

    @Insert
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}