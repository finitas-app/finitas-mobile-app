package pl.finitas.app.core.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.core.domain.validateBuilder
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryState
import java.util.UUID

class SpendingCategoryService(
    private val spendingCategoryRepository: SpendingCategoryRepository,
    private val userStoreRepository: UserStoreRepository,
) {
    suspend fun getSpendingCategoryById(idSpendingCategory: UUID): SpendingCategoryState {
        return spendingCategoryRepository.getSpendingCategoryBy(idSpendingCategory).let {
            SpendingCategoryState(
                it.name,
                it.idParent,
                it.idCategory,
            )
        }
    }

    @Throws(InputValidationException::class)
    suspend fun upsertSpendingCategory(upsertSpendingCategoryCommand: UpsertSpendingCategoryCommand) {
        validateBuilder {
            validate(upsertSpendingCategoryCommand.name.isNotBlank(), "title") {
                "Title cannot be empty."
            }
        }
        val model = upsertSpendingCategoryCommand.toModel()
        spendingCategoryRepository.upsertSpendingCategory(model)
        try {
            userStoreRepository.changeCategories(listOf(model))
        } catch (_: Exception) {
        }
    }

    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategoryView>> =
        spendingCategoryRepository.getSpendingCategoriesFlow().map { categories ->
            val (originCategories, nestedCategories) = categories.partition { it.idParent == null }
            val categoriesByParentId = nestedCategories.groupBy { it.idParent!! }.toMutableMap()

            getSpendingCategoriesTreeRecursive(originCategories, categoriesByParentId)
        }


    suspend fun getSpendingCategoriesByIdUserFlat(idUser: UUID? = null): List<SpendingCategoryView> {
        val categories =
            if (idUser == null) {
                spendingCategoryRepository.getSpendingCategories()
            } else {
                spendingCategoryRepository.getSpendingCategoriesByIdUser(idUser)
            }

        val (originCategories, nestedCategories) = categories.partition { it.idParent == null }
        val categoriesByParentId = nestedCategories.groupBy { it.idParent }.toMutableMap()

        val result = originCategories.sortedBy { it.name }.toMutableList()
        var index = 0
        while (index < result.size && categoriesByParentId.isNotEmpty()) {
            val currentCategoryId = result[index].idCategory
            val temp = categoriesByParentId[currentCategoryId]
            if (temp != null) {
                categoriesByParentId.remove(currentCategoryId)
                result.addAll(index + 1, temp.sortedBy { it.name })
            }
            index++
        }


        return result.map {
            SpendingCategoryView(
                it.name,
                listOf(),
                it.idCategory,
            )
        }

    }
}

private fun getSpendingCategoriesTreeRecursive(
    currentCategories: List<SpendingCategory>,
    categoriesByParentId: Map<UUID, List<SpendingCategory>>,
): List<SpendingCategoryView> = currentCategories.sortedBy { it.name }.map {
    SpendingCategoryView(
        name = it.name,
        idCategory = it.idCategory,
        elements = getSpendingCategoriesTreeRecursive(
            categoriesByParentId[it.idCategory] ?: listOf(),
            categoriesByParentId,
        )
    )
}

private fun UpsertSpendingCategoryCommand.toModel() = SpendingCategory(
    idCategory = idSpendingCategory ?: UUID.randomUUID(),
    name = name,
    idParent = idParentCategory,
    idUser = null,
    version = null,
    isDeleted = false,
)