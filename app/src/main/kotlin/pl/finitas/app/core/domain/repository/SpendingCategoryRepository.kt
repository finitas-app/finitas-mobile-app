package pl.finitas.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.SpendingCategory
import java.util.UUID

interface SpendingCategoryRepository {

    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    suspend fun getSpendingCategories(): List<SpendingCategory>

    suspend fun findSpendingCategoryBy(idCategory: UUID): SpendingCategory?

    @Throws(SpendingCategoryNotFoundException::class)
    suspend fun getSpendingCategoryBy(idCategory: UUID) =
        findSpendingCategoryBy(idCategory) ?: throw SpendingCategoryNotFoundException(idCategory)

    suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory)

    suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory)
}


class SpendingCategoryNotFoundException(idSpendingCategory: UUID) :
    Exception("Spending category with id:$idSpendingCategory not found!")