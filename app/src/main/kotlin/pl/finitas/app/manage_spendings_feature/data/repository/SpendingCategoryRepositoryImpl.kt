package pl.finitas.app.manage_spendings_feature.data.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.data.data_source.SpendingCategoryDao
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingCategory
import pl.finitas.app.manage_spendings_feature.domain.repository.SpendingCategoryRepository

class SpendingCategoryRepositoryImpl(
    private val dao: SpendingCategoryDao,
): SpendingCategoryRepository {
    override fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>> {
        return dao.getSpendingCategoriesFlow()
    }

    override suspend fun getSpendingCategories(): List<SpendingCategory> {
        return dao.getSpendingCategories()
    }

    override suspend fun findSpendingCategoryBy(idCategory: Int): SpendingCategory? {
        return dao.findSpendingCategoryBy(idCategory)
    }

    override suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory) {
        dao.upsertSpendingCategory(spendingCategory)
    }

    override suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory) {
        dao.deleteSpendingCategory(spendingCategory)
    }
}