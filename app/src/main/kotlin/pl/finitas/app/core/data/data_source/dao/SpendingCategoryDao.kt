package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.SpendingCategory
import java.util.UUID

@Dao
interface SpendingCategoryDao {

    @Query("SELECT * FROM SpendingCategory WHERE idUser is null")
    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    @Query("SELECT * FROM SpendingCategory")
    fun getSpendingCategoriesOfAllUsersFlow(): Flow<List<SpendingCategory>>

    @Query("SELECT * FROM SpendingCategory WHERE idUser is null")
    suspend fun getSpendingCategories(): List<SpendingCategory>

    @Query("SELECT * FROM SpendingCategory")
    fun getAllUsersSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    @Query("SELECT * FROM SpendingCategory")
    suspend fun getAllUsersSpendingCategories(): List<SpendingCategory>

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

    @Query("DELETE FROM SpendingCategory WHERE idCategory in (:idsSpendingCategory)")
    suspend fun deleteSpendingCategoryBy(idsSpendingCategory: List<UUID>)

    @Upsert
    suspend fun upsertSpendingCategories(spendingCategories: List<SpendingCategory>)

    @Query("SELECT * FROM SpendingCategory WHERE version is null")
    suspend fun getChangedCategories(): List<SpendingCategory>

    @Query("SELECT * FROM SpendingCategory WHERE idUser = :idUser")
    suspend fun getSpendingCategoriesByIdUser(idUser: UUID): List<SpendingCategory>


    @Transaction
    suspend fun deleteByIdUser(idUser: UUID) {
        deleteSpendingCategoryBy(getSpendingCategoriesByIdUser(idUser).map { it.idCategory })
    }
}