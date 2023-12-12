package pl.finitas.app.manage_spendings_feature.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.repository.SpendingCategoryNotFoundException
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.repository.TotalSpendingRepository
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecordDto
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.TotalSpendingState
import java.time.LocalDate
import java.util.UUID

class FinishedSpendingService(
    private val totalSpendingRepository: TotalSpendingRepository,
    private val spendingCategoryRepository: SpendingCategoryRepository,
) {

    fun getTotalSpendings(): Flow<List<Pair<LocalDate, List<TotalSpendingView>>>> =
        totalSpendingRepository.getFinishedSpendings().map { totalSpendings ->
            val categories =
                spendingCategoryRepository
                .getSpendingCategories()
                .associateBy { it.idCategory }


            return@map totalSpendings
                .groupBy(keySelector = { it.purchaseDate.toLocalDate() }) { totalSpendingWithRecords ->
                    val (
                        idTotalSpending,
                        title,
                        time,
                        spendingRecords,
                    ) = totalSpendingWithRecords
                    val recordsByCategory = spendingRecords.groupBy { it.idCategory }

                    TotalSpendingView(
                        idTotalSpending = idTotalSpending,
                        name = title,
                        date = time,
                        spendingElements = recordsByCategory.map { (idCategory, records) ->
                            SpendingCategoryView(
                                categories[idCategory]?.name
                                    ?: throw SpendingCategoryNotFoundException(idCategory),
                                idCategory,
                                records.map { record ->
                                    SpendingRecordView(
                                        record.name,
                                        record.price,
                                        idSpendingRecord = record.idSpendingRecord!!,
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

    suspend fun addTotalSpending(totalSpending: TotalSpendingState) {
        totalSpendingRepository.upsertFinishedSpendingWithRecords(
            totalSpending.toTotalSpendingWithRecords()
        )
    }
}


// TODO: refactoring
private fun TotalSpendingView.normalizeTotalSpendingView(categoryById: Map<UUID, SpendingCategory>): TotalSpendingView {
    val recordsByCategoryId = spendingElements.associate { element ->
        (element as SpendingCategoryView).let { it.idCategory to it.spendingElements }
    }.toMutableMap()
        /*.groupBy {
        (it as? SpendingRecordView)?.idCategory ?: -1
    }.toMutableMap()*/
    val result = mutableListOf<SpendingElement>()
    val previousSpendingElements = mutableMapOf<UUID, SpendingCategoryView>()

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
            verifyPrevious(previousSpendingElements, currentCategory.idParent)
        if (possiblePrevious != null) {
            possiblePrevious += currentSpendingElement
            continue
        }

        while (currentCategory.idParent != null) {
            currentCategory =
                categoryById[currentCategory.idParent] ?: throw SpendingCategoryNotFoundException(
                    currentCategory.idParent!!
                )
            val categoryId = currentCategory.idCategory
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
                verifyPrevious(previousSpendingElements, currentCategory.idParent)
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
    previous: Map<UUID, SpendingCategoryView>,
    categoryId: UUID?,
): MutableList<SpendingElement>? {
    return previous[categoryId]?.let { it.spendingElements as? MutableList<SpendingElement> }
}

@Throws(InvalidTotalSpendingState::class)
fun TotalSpendingState.toTotalSpendingWithRecords(): FinishedSpendingWithRecordsDto {
    val generatedUUID = idTotalSpending ?: UUID.randomUUID()

    return FinishedSpendingWithRecordsDto(
        idSpendingSummary = generatedUUID,
        title = title,
        purchaseDate = date.atStartOfDay(),
        spendingRecords = categories.flatMap { category ->
            category.spendingElements.map { spendingRecord ->
                if (spendingRecord !is SpendingRecordView) throw  InvalidTotalSpendingState(this)

                SpendingRecordDto(
                    name = spendingRecord.name,
                    price = spendingRecord.totalPrice,
                    idCategory = category.idCategory,
                    idSpendingSummary = generatedUUID,
                    idSpendingRecord = spendingRecord.idSpendingRecord,
                )
            }
        }
    )
}


class InvalidTotalSpendingState(totalSpendingView: TotalSpendingState) :
    Exception("Total spending state is invalid: $totalSpendingView")
