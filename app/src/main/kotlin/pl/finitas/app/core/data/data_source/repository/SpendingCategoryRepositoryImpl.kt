package pl.finitas.app.core.data.data_source.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.SpendingCategoryDao
import pl.finitas.app.core.data.data_source.dao.UserDao
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.repository.CategoryVersionDto
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import java.util.UUID

class SpendingCategoryRepositoryImpl(
    private val dao: SpendingCategoryDao,
    private val userDao: UserDao,
): SpendingCategoryRepository {
    override fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>> {
        return dao.getSpendingCategoriesFlow()
    }

    override fun getSpendingCategoriesOfAllUsersFlow(): Flow<List<SpendingCategory>> {
        return dao.getSpendingCategoriesOfAllUsersFlow()
    }

    override suspend fun getSpendingCategories(): List<SpendingCategory> {
        return dao.getSpendingCategories()
    }

    override suspend fun getSpendingCategoriesByIdUser(idUser: UUID): List<SpendingCategory> {
        return dao.getSpendingCategoriesByIdUser(idUser)
    }

    override suspend fun getAllUsersSpendingCategories(): List<SpendingCategory> {
        return dao.getAllUsersSpendingCategories()
    }

    override suspend fun deleteByIdUser(idUser: UUID) {
        dao.deleteByIdUser(idUser)
    }

    override fun getAllUsersSpendingCategoriesFlow(): Flow<List<SpendingCategory>> {
        return dao.getAllUsersSpendingCategoriesFlow()
    }

    override suspend fun findSpendingCategoryBy(idCategory: UUID): SpendingCategory? {
        return dao.findSpendingCategoryBy(idCategory)
    }

    override suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory) {
        dao.upsertSpendingCategory(spendingCategory)
    }

    override suspend fun upsertSpendingCategories(spendingCategories: List<SpendingCategory>) {
        dao.upsertSpendingCategories(spendingCategories)
    }

    override suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory) {
        dao.deleteSpendingCategory(spendingCategory)
    }

    override suspend fun deleteSpendingCategoriesBy(idsSpendingCategory: List<UUID>) {
        dao.deleteSpendingCategoryBy(idsSpendingCategory)
    }

    override suspend fun setCategoryVersions(versions: List<CategoryVersionDto>) {
        versions.forEach { userDao.setCategoryVersion(it.idUser, it.version) }
    }

    override suspend fun getChangedCategories(): List<SpendingCategory> {
        return dao.getChangedCategories()
    }
}