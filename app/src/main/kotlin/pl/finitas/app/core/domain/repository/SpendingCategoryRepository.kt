package pl.finitas.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.SpendingCategory
import java.util.UUID

interface SpendingCategoryRepository {

    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    suspend fun getSpendingCategories(): List<SpendingCategory>

    suspend fun getSpendingCategoriesByIdUser(idUser: UUID): List<SpendingCategory>

    fun getAllUsersSpendingCategoriesFlow(): Flow<List<SpendingCategory>>

    suspend fun findSpendingCategoryBy(idCategory: UUID): SpendingCategory?

    @Throws(SpendingCategoryNotFoundException::class)
    suspend fun getSpendingCategoryBy(idCategory: UUID) =
        findSpendingCategoryBy(idCategory) ?: throw SpendingCategoryNotFoundException(idCategory)

    suspend fun upsertSpendingCategory(spendingCategory: SpendingCategory)
    suspend fun upsertSpendingCategories(spendingCategories: List<SpendingCategory>)

    suspend fun deleteSpendingCategory(spendingCategory: SpendingCategory)

    suspend fun deleteSpendingCategoriesBy(idsSpendingCategory: List<UUID>)

    suspend fun setCategoryVersions(versions: List<CategoryVersionDto>)

    suspend fun getChangedCategories(): List<SpendingCategory>
    fun getSpendingCategoriesOfAllUsersFlow(): Flow<List<SpendingCategory>>
    suspend fun getAllUsersSpendingCategories(): List<SpendingCategory>
    suspend fun deleteByIdUser(idUser: UUID)
}


class SpendingCategoryNotFoundException(idSpendingCategory: UUID) :
    Exception("Spending category with id:$idSpendingCategory not found!")