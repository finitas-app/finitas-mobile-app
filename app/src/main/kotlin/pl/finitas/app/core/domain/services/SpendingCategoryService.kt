package pl.finitas.app.core.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.manage_additional_elements.presentation.spending_category.SpendingCategoryState
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryView
import java.util.UUID

class SpendingCategoryService(
    private val spendingCategoryRepository: SpendingCategoryRepository,
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

    suspend fun upsertSpendingCategory(upsertSpendingCategoryCommand: UpsertSpendingCategoryCommand) {
        spendingCategoryRepository.upsertSpendingCategory(upsertSpendingCategoryCommand.toModel())
    }

    fun getSpendingCategoriesFlow(): Flow<List<SpendingCategoryView>> =
        spendingCategoryRepository.getSpendingCategoriesFlow().map { categories ->
            val (originCategories, nestedCategories) = categories.partition { it.idParent == null }
            val categoriesByParentId = nestedCategories.groupBy { it.idParent!! }.toMutableMap()

            getSpendingCategoriesTreeRecursive(originCategories, categoriesByParentId)
        }


    suspend fun getSpendingCategoriesFlat(): List<SpendingCategoryView> {
        val categories = spendingCategoryRepository.getSpendingCategories()

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
                it.idCategory,
                listOf(),
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
        spendingElements = getSpendingCategoriesTreeRecursive(
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
)