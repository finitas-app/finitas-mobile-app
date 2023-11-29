package pl.finitas.app.manage_spendings_feature.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingCategory
import pl.finitas.app.manage_spendings_feature.domain.repository.SpendingCategoryNotFoundException
import pl.finitas.app.manage_spendings_feature.domain.repository.SpendingCategoryRepository
import pl.finitas.app.manage_spendings_feature.domain.repository.TotalSpendingRepository
import java.time.LocalDate

class TotalSpendingService(
    private val totalSpendingRepository: TotalSpendingRepository,
    private val spendingCategoryRepository: SpendingCategoryRepository,
) {

    fun getTotalSpendings(): Flow<List<Pair<LocalDate, List<TotalSpendingView>>>> =
        totalSpendingRepository.getTotalSpendings().map { totalSpendings ->
            val categories =
                spendingCategoryRepository
                .getSpendingCategories()
                .associateBy { it.idCategory ?: -1 }


            return@map totalSpendings
                .groupBy(keySelector = { it.totalSpending.time.toLocalDate() }) { totalSpendingWithRecords ->
                    val (totalSpending, spendingRecords) = totalSpendingWithRecords
                    val recordsByCategory = spendingRecords.groupBy { it.idCategory }

                    TotalSpendingView(
                        idTotalSpending = totalSpending.idTotalSpending ?: -1,
                        name = totalSpending.title,
                        date = totalSpending.time,
                        spendingElements = recordsByCategory.map { (idCategory, records) ->
                            SpendingCategoryView(
                                categories[idCategory]?.name
                                    ?: throw SpendingCategoryNotFoundException(idCategory),
                                idCategory,
                                records.map { record ->
                                    SpendingRecordView(
                                        record.name,
                                        record.price,
                                        record.idCategory,
                                    )
                                }
                            )
                        }
                    )
                }
                .map { (date, totalSpendingsByDay) ->
                    date to totalSpendingsByDay.map { it.normalizeTotalSpendingView(categories) }
                        .sortedByDescending { it.date }
                }
                .sortedByDescending { it.first }
        }
}


// TODO: refactoring
private fun TotalSpendingView.normalizeTotalSpendingView(categoryById: Map<Int, SpendingCategory>): TotalSpendingView {
    val recordsByCategoryId = spendingElements.associate { element ->
        (element as pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryView).let { it.idCategory to it.spendingElements }
    }.toMutableMap()
        /*.groupBy {
        (it as? SpendingRecordView)?.idCategory ?: -1
    }.toMutableMap()*/
    val result = mutableListOf<SpendingElement>()
    val previousSpendingElements = mutableMapOf<Int, pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryView>()

    outer@ while (recordsByCategoryId.isNotEmpty()) {
        val (idCategory, records) = recordsByCategoryId.entries.first()
        var currentCategory =
            categoryById[idCategory] ?: throw SpendingCategoryNotFoundException(idCategory)
        var currentSpendingElement = SpendingCategoryView(
            currentCategory.name,
            idCategory,
            records,
        )
        previousSpendingElements[idCategory] = currentSpendingElement
        val possiblePrevious =
            verifyPrevious(previousSpendingElements, currentCategory.idParent ?: -1)
        if (possiblePrevious != null) {
            possiblePrevious += currentSpendingElement
            continue
        }

        while (currentCategory.idParent != null) {
            currentCategory =
                categoryById[currentCategory.idParent] ?: throw SpendingCategoryNotFoundException(
                    currentCategory.idParent ?: -1
                )
            val categoryId = currentCategory.idCategory ?: -1
            val newContainers: MutableList<SpendingElement> = mutableListOf(currentSpendingElement)
            if (currentCategory.idCategory in recordsByCategoryId) {
                newContainers.addAll(recordsByCategoryId[categoryId]!!)
                recordsByCategoryId.remove(categoryId)
            }
            currentSpendingElement = SpendingCategoryView(
                currentCategory.name,
                categoryId,
                newContainers,
            )
            previousSpendingElements[idCategory] = currentSpendingElement

            val possiblePreviousInner =
                verifyPrevious(previousSpendingElements, currentCategory.idParent ?: -1)
            if (possiblePreviousInner != null) {
                possiblePreviousInner += currentSpendingElement
                continue@outer
            }
        }
        result += currentSpendingElement
        recordsByCategoryId.remove(idCategory)
    }

    return copy(spendingElements = spendingElements)
}

private fun verifyPrevious(
    previous: Map<Int, pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryView>,
    categoryId: Int,
): MutableList<SpendingElement>? {
    return previous[categoryId]?.let { it.spendingElements as? MutableList<SpendingElement> }
}