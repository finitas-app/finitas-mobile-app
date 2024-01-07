package pl.finitas.app.manage_spendings_feature.domain.service

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.dto.store.DeleteFinishedSpendingRequest
import pl.finitas.app.core.domain.dto.store.RemoteFinishedSpendingDto
import pl.finitas.app.core.domain.dto.store.RemoteSpendingRecordDto
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.repository.FinishedSpendingStoreRepository
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.SpendingCategoryNotFoundException
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.core.domain.validateBuilder
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTotalSpendings(idsUser: List<UUID>): Flow<List<Pair<LocalDate, List<FinishedSpendingView>>>> {
        return profileRepository.getAuthorizedUserId().flatMapLatest { currentUser ->
            when {
                idsUser.isEmpty() || (idsUser.size == 1 && idsUser[0] == currentUser) -> {
                    finishedSpendingRepository.getFinishedSpendings(listOf()).combine(
                        spendingCategoryRepository
                            .getSpendingCategoriesFlow(),
                        transform = ::mapToView,
                    )
                }

                idsUser.size == 1 && idsUser[0] != currentUser -> {
                    finishedSpendingRepository.getFinishedSpendingsByIdUser(idsUser[0]).combine(
                        spendingCategoryRepository
                            .getSpendingCategoriesOfAllUsersFlow(),
                        transform = ::mapToView,
                    )
                }

                else -> {
                    finishedSpendingRepository.getFinishedSpendings(idsUser).combine(
                        spendingCategoryRepository
                            .getSpendingCategoriesOfAllUsersFlow(),
                        transform = ::mapToView,
                    )
                }
            }
        }
    }


    suspend fun upsertFinishedSpending(finishedSpendingState: FinishedSpendingState) {
        validateBuilder {
            validate(finishedSpendingState.title.isNotBlank(), "title") { "Title cannot be empty." }
            validate(finishedSpendingState.categories.flatMap { it.elements }.isNotEmpty()) {
                "You must add at least one item of spending."
            }
            validateForSingleOutput(finishedSpendingState.categories.isNotEmpty()) {
                if (finishedSpendingState.idUser == null) {
                    "To add a spending, you must first create a category."
                } else {
                    "The user you are trying to add the spending to has no categories, you will" +
                            " only be able to do this after the user has added their first category."
                }
            }
        }
        val currentUser = profileRepository.getAuthorizedUserId().first()
        val dto = finishedSpendingState.toTotalSpendingWithRecords()
        if (dto.idUser == null || dto.idUser == currentUser) {
            finishedSpendingRepository.upsertFinishedSpendingWithRecords(dto)
            if (currentUser != null) {
                try {
                    finishedSpendingStoreRepository.changeFinishedSpendings(
                        listOf(dto.toRemote(currentUser))
                    )
                } catch (_: Exception) {

                }
            }
        } else if (currentUser != null) {
            try {
                val remoteDto = dto.toRemote(dto.idUser)
                if (finishedSpendingState.idFinishedSpending == null) {
                    finishedSpendingStoreRepository.createFinishedSpending(remoteDto)
                } else {
                    finishedSpendingStoreRepository.updateFinishedSpending(remoteDto)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw InputValidationException(
                    "An error occurred, check your internet connection.",
                )
            }
        }
    }

    suspend fun deleteFinishedSpending(idFinishedSpending: UUID) {
        val currentUser = profileRepository.getAuthorizedUserId().first()
        val finishedSpending = finishedSpendingRepository.getById(idFinishedSpending)
        if (
            finishedSpending.version == null
        ) {
            finishedSpendingRepository.deleteFinishedSpendingById(idFinishedSpending)
            return
        }

        if (finishedSpending.idUser == null) {
            finishedSpendingRepository.markAsDeleted(idFinishedSpending)
            if (currentUser != null) {
                try {
                    finishedSpendingStoreRepository.deleteFinishedSpending(
                        DeleteFinishedSpendingRequest(
                            idFinishedSpending,
                            currentUser,
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return
        }
        //TODO: maybe disable for all not user data and just remove from database
        try {
            finishedSpendingStoreRepository.deleteFinishedSpending(
                DeleteFinishedSpendingRequest(
                    idFinishedSpending,
                    finishedSpending.idUser,
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw InputValidationException(
                "An error occurred, check your internet connection.",
            )
        }
    }
}

private fun mapToView(
    totalSpendings: List<FinishedSpendingWithRecordsDto>,
    categories: List<SpendingCategory>,
): List<Pair<LocalDate, List<FinishedSpendingView>>> {
    val categoriesById = categories
        .associateBy { it.idCategory }


    return totalSpendings
        .groupBy(keySelector = { it.purchaseDate.toLocalDate() }) { totalSpendingWithRecords ->
            val (
                idTotalSpending,
                title,
                time,
                currencyValue,
                _,
                _,
                idUser,
                spendingRecords,
            ) = totalSpendingWithRecords
            val recordsByCategory = spendingRecords.groupBy { it.idCategory }

            FinishedSpendingView(
                idFinishedSpending = idTotalSpending,
                name = title,
                date = time,
                idUser = idUser,
                currency = currencyValue,
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
                                currency = currencyValue,
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


// TODO: refactoring
private fun FinishedSpendingView.normalizeTotalSpendingView(categoryById: Map<UUID, SpendingCategory>): FinishedSpendingView {
    val recordsByCategoryId = elements.associate { element ->
        (element as SpendingCategoryView).let { it.idCategory to it.elements }
    }.toMutableMap()

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
        currencyValue = currencyValue,
        isDeleted = false,
        idReceipt = null,
        idUser = idUser,
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
        currency = currencyValue,
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
