package pl.finitas.app.manage_additional_elements_feature.domain.services

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto
import pl.finitas.app.manage_additional_elements_feature.domain.SpendingRecordDto
import pl.finitas.app.manage_additional_elements_feature.domain.repositories.RegularSpendingRepository
import pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending.RegularSpendingState
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.TimeUnit

class RegularSpendingService(val repository: RegularSpendingRepository) {
    private fun getSpendingRecordsFrom(
        categories: List<SpendingCategoryView>,
        idSpendingSummary: UUID,
    ) = categories
        .flatMap { it.elements }
        .map {
            it as SpendingRecordView
            SpendingRecordDto(
                name = it.name,
                price = it.totalPrice,
                idCategory = it.idCategory,
                idSpendingRecord = it.idSpendingRecord,
                idSpendingSummary = idSpendingSummary
            )
        }

    private fun getCategoryViewsFrom(
        spendingRecords: List<SpendingRecordDto>,
        allCategories: List<SpendingCategoryView>,
    ): List<SpendingCategoryView> {
        val spendingRecordsAssociatedByIdCategory = spendingRecords.groupBy { it.idCategory }
        return allCategories
            .associateBy { it.idCategory }
            .map {
                val (idCategory, categoryView) = it
                SpendingCategoryView(
                    elements = spendingRecordsAssociatedByIdCategory[idCategory]
                        ?.map { dto ->
                            SpendingRecordView(
                                name = dto.name,
                                totalPrice = dto.price,
                                idSpendingRecord = dto.idSpendingRecord,
                                idCategory = dto.idCategory,
                            )
                        } ?: listOf(),
                    name = categoryView.name,
                    idCategory = idCategory
                )
            }
    }

    fun getRegularSpendingsFlow() = repository.getRegularSpendingsFlow()

    suspend fun upsertRegularSpendingWithRecords(regularSpendingState: RegularSpendingState) {
        val idSpendingSummary = regularSpendingState.idSpendingSummary ?: UUID.randomUUID()

        repository.upsertRegularSpendingWithRecords(
            regularSpendingDto = RegularSpendingWithSpendingDataDto(
                idSpendingSummary = idSpendingSummary,
                actualizationPeriod = regularSpendingState.actualizationPeriod.toInt(),
                periodUnit = regularSpendingState.periodUnit,
                name = regularSpendingState.title,
                lastActualizationDate = regularSpendingState.lastActualizationDate,
                spendingRecords = getSpendingRecordsFrom(
                    categories = regularSpendingState.categories,
                    idSpendingSummary = idSpendingSummary
                )
            ),
        )
    }

    fun getStateFrom(
        regularSpending: RegularSpendingWithSpendingDataDto,
        allCategories: List<SpendingCategoryView>,
    ) = RegularSpendingState(
        title = regularSpending.name,
        idSpendingSummary = regularSpending.idSpendingSummary,
        periodUnit = regularSpending.periodUnit,
        actualizationPeriod = regularSpending.actualizationPeriod.toUInt(),
        lastActualizationDate = regularSpending.lastActualizationDate,
        categories = getCategoryViewsFrom(
            spendingRecords = regularSpending.spendingRecords,
            allCategories = allCategories
        )
    )

    suspend fun deleteRegularSpendingWithRecords(regularSpending: RegularSpendingWithSpendingDataDto) =
        repository.deleteRegularSpendingWithRecords(regularSpending)
}