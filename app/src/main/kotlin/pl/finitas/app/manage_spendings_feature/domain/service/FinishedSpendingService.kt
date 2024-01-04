package pl.finitas.app.manage_spendings_feature.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.dto.store.RemoteFinishedSpendingDto
import pl.finitas.app.core.domain.dto.store.RemoteSpendingRecordDto
import pl.finitas.app.core.domain.repository.FinishedSpendingStoreRepository
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.SpendingCategoryNotFoundException
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecordDto
import pl.finitas.app.manage_spendings_feature.domain.repository.FinishedSpendingRepository
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.FinishedSpendingState
import java.time.LocalDate
import java.util.UUID

class FinishedSpendingService(
    private val finishedSpendingRepository: FinishedSpendingRepository,
    private val spendingCategoryRepository: SpendingCategoryRepository,
    private val finishedSpendingStoreRepository: FinishedSpendingStoreRepository,
    private val profileRepository: ProfileRepository,
) {

    fun getTotalSpendings(): Flow<List<Pair<LocalDate, List<FinishedSpendingView>>>> =
        finishedSpendingRepository.getFinishedSpendings().combine(
            spendingCategoryRepository
                .getSpendingCategoriesOfAllUsersFlow()
        ) { totalSpendings, categories ->
            val categoriesById = categories
                .associateBy { it.idCategory }


            totalSpendings
                .groupBy(keySelector = { it.purchaseDate.toLocalDate() }) { totalSpendingWithRecords ->
                    val (
                        idTotalSpending,
                        title,
                        time,
                        _,
                        _,
                        spendingRecords,
                    ) = totalSpendingWithRecords
                    val recordsByCategory = spendingRecords.groupBy { it.idCategory }

                    FinishedSpendingView(
                        idFinishedSpending = idTotalSpending,
                        name = title,
                        date = time,
                        elements = recordsByCategory.map { (idCategory, records) ->
                            SpendingCategoryView(
                                name = categoriesById[idCategory]?.name
                                    ?: throw SpendingCategoryNotFoundException(idCategory),
                                idCategory = idCategory,
                                elements = records.map { record ->
                                    SpendingRecordView(
                                        name = record.name,
                                        totalPrice = record.price,
                                        idSpendingRecord = record.idSpendingRecord,
                                        idCategory = record.idCategory,
                                    )
                                }
                            )
                        }
                    )
                }
                .map { (date, totalSpendingsByDay) ->
                    date to totalSpendingsByDay.map { it.normalizeTotalSpendingView(categoriesById) }
                        .sortedByDescending { it.date }
                }
                .sortedByDescending { it.first }
        }

    suspend fun upsertTotalSpending(totalSpending: FinishedSpendingState) {
        val dto = totalSpending.toTotalSpendingWithRecords()
        finishedSpendingRepository.upsertFinishedSpendingWithRecords(dto)
        val currentUser = profileRepository.getAuthorizedUserId().first() ?: return
        try {
            finishedSpendingStoreRepository.changeFinishedSpendings(
                listOf(dto.toRemote(currentUser))
            )
        } catch (_: Exception) {

        }
    }

    suspend fun deleteFinishedSpending(idFinishedSpending: UUID) {
        finishedSpendingRepository.deleteFinishedSpending(idFinishedSpending)
    }
}


// TODO: refactoring
private fun FinishedSpendingView.normalizeTotalSpendingView(categoryById: Map<UUID, SpendingCategory>): FinishedSpendingView {
    val recordsByCategoryId = elements.associate { element ->
        (element as SpendingCategoryView).let { it.idCategory to it.elements }
    }.toMutableMap()
    /*.groupBy {
    (it as? SpendingRecordView)?.idCategory ?: -1
}.toMutableMap()*/
    val result = mutableListOf<SpendingElementView>()
    val previousSpendingElements = mutableMapOf<UUID, SpendingCategoryView>()

    outer@ while (recordsByCategoryId.isNotEmpty()) {
        val (idCategory, records) = recordsByCategoryId.entries.first()
        var currentCategory =
            categoryById[idCategory] ?: throw SpendingCategoryNotFoundException(idCategory)
        var currentSpendingElement = SpendingCategoryView(
            currentCategory.name,
            records,
            idCategory,
        )
        previousSpendingElements[idCategory] = currentSpendingElement
        val possiblePrevious =
            verifyPrevious(previousSpendingElements, currentCategory.idParent)
        if (possiblePrevious != null) {
            possiblePrevious += currentSpendingElement
            recordsByCategoryId.remove(idCategory)
            continue
        }

        while (currentCategory.idParent != null) {
            currentCategory =
                categoryById[currentCategory.idParent] ?: throw SpendingCategoryNotFoundException(
                    currentCategory.idParent!!
                )
            val categoryId = currentCategory.idCategory
            val newContainers: MutableList<SpendingElementView> =
                mutableListOf(currentSpendingElement)
            if (currentCategory.idCategory in recordsByCategoryId) {
                newContainers.addAll(recordsByCategoryId[categoryId]!!)
                recordsByCategoryId.remove(categoryId)
            }
            currentSpendingElement = SpendingCategoryView(
                currentCategory.name,
                newContainers,
                categoryId,
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

    return copy(elements = result)
}

private fun verifyPrevious(
    previous: Map<UUID, SpendingCategoryView>,
    categoryId: UUID?,
): MutableList<SpendingElementView>? {
    return previous[categoryId]?.let { it.elements as? MutableList<SpendingElementView> }
}

@Throws(InvalidFinishedSpendingState::class)
fun FinishedSpendingState.toTotalSpendingWithRecords(): FinishedSpendingWithRecordsDto {
    val idSpendingSummary = idFinishedSpending ?: UUID.randomUUID()
    return FinishedSpendingWithRecordsDto(
        idSpendingSummary = idSpendingSummary,
        title = title,
        purchaseDate = date.atStartOfDay(),
        isDeleted = false,
        idReceipt = null,
        spendingRecords = categories.flatMap { category ->
            category.elements.map { spendingRecord ->
                if (spendingRecord !is SpendingRecordView) throw InvalidFinishedSpendingState(this)

                SpendingRecordDto(
                    name = spendingRecord.name,
                    price = spendingRecord.totalPrice,
                    idCategory = category.idCategory,
                    idSpendingSummary = idSpendingSummary,
                    idSpendingRecord = spendingRecord.idSpendingRecord,
                )
            }
        }
    )
}

private fun FinishedSpendingWithRecordsDto.toRemote(idUser: UUID): RemoteFinishedSpendingDto {
    return RemoteFinishedSpendingDto(
        idSpendingSummary = idSpendingSummary,
        idReceipt = idReceipt,
        purchaseDate = purchaseDate,
        version = 0,
        idUser = idUser,
        isDeleted = isDeleted,
        name = title,
        spendingRecords = spendingRecords.map {
            RemoteSpendingRecordDto(
                idSpendingRecordData = it.idSpendingRecord,
                name = it.name,
                price = it.price,
                idCategory = it.idCategory,
            )
        }
    )
}

class InvalidFinishedSpendingState(finishedSpendingView: FinishedSpendingState) :
    Exception("Finished spending state is invalid: $finishedSpendingView")
