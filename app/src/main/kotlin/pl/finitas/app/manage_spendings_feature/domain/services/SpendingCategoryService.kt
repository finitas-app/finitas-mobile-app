package pl.finitas.app.manage_spendings_feature.domain.services

import pl.finitas.app.core.domain.repository.SpendingCategoryRepository

class SpendingCategoryService(
    private val spendingCategoryRepository: SpendingCategoryRepository,
) {

    // TODO: Change to flow
    suspend fun getSpendingCategories(): List<SpendingCategoryView> {
        val categories = spendingCategoryRepository.getSpendingCategories()

        val (originCategories, nestedCategories) = categories.partition { it.idParent == null }
        val categoriesByParentId = nestedCategories.groupBy { it.idParent }.toMutableMap()
        val result = originCategories.toMutableList()

        var index = 0
        while (index < nestedCategories.size && categoriesByParentId.isNotEmpty()) {
            val currentCategoryId = result[index].idCategory!!
            val temp = categoriesByParentId[currentCategoryId]
            if (temp != null) {
                categoriesByParentId.remove(currentCategoryId)
                result.addAll(index + 1, temp)
            }
            index++
        }


        return result.map {
            SpendingCategoryView(
                it.name,
                it.idCategory!!,
                listOf(),
            )
        }
    }
}