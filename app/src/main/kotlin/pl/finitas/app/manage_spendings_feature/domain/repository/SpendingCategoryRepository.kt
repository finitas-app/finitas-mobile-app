package pl.finitas.app.manage_spendings_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingCategory

interface SpendingCategoryRepository {

    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    suspend fun getSpendingCategories(): List<SpendingCategory>

    suspend fun findSpendingCategoryBy(idCategory: Int): SpendingCategory?

    @Throws(SpendingCategoryNotFoundException::class)
    suspend fun getSpendingCategoryBy(idCategory: Int) =
        findSpendingCategoryBy(idCategory) ?: throw SpendingCategoryNotFoundException(idCategory)

    suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory)

    suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory)
}


class SpendingCategoryNotFoundException(idSpendingCategory: Int) :
    Exception("Spending category with id:$idSpendingCategory not found!")