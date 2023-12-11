package pl.finitas.app.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.SpendingCategoryDao
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import java.util.UUID

class SpendingCategoryRepositoryImpl(
    private val dao: SpendingCategoryDao,
): SpendingCategoryRepository {
    override fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>> {
        return dao.getSpendingCategoriesFlow()
    }

    override suspend fun getSpendingCategories(): List<SpendingCategory> {
        return dao.getSpendingCategories()
    }

    override suspend fun findSpendingCategoryBy(idCategory: UUID): SpendingCategory? {
        return dao.findSpendingCategoryBy(idCategory)
    }

    override suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory) {
        dao.upsertSpendingCategory(spendingCategory)
    }

    override suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory) {
        dao.deleteSpendingCategory(spendingCategory)
    }
}