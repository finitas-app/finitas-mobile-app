package pl.finitas.app.manage_additional_elements_feature.domain.services

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto
import pl.finitas.app.manage_additional_elements_feature.domain.repositories.RegularSpendingActualizationRepository
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.TimeUnit

class Actualizator(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {

    val repository: RegularSpendingActualizationRepository by inject()

    @WorkerThread
    override fun doWork(): Result {
        runBlocking {
            println("INFO: regular spendings actualization start")
            repository.getRegularSpendings()
                .apply {
                    println("INFO: successfully retrieved regular spendings: ${this.size}")
                }
                .filter {
                    LocalDate.now() >= getExpectedActualizationDate(it).toLocalDate()
                }
                .map(::mapRegularSpendingToFinishedSpending)
                .forEach {
                    println(
                        "INFO: creating new finished spending. Name - ${it.title}, id - ${it.idSpendingSummary}"
                    )
                    repository.upsertFinishedSpendingWithRecords(it)
                }
        }

        return Result.success()
    }

    private fun mapRegularSpendingToFinishedSpending(
        regularSpending: RegularSpendingWithSpendingDataDto
    ): FinishedSpendingWithRecordsDto {
        val idSpendingSummary = UUID.randomUUID()
        return FinishedSpendingWithRecordsDto(
            idSpendingSummary = idSpendingSummary,
            title = regularSpending.name,
            purchaseDate = getExpectedActualizationDate(regularSpending),
            currencyValue = regularSpending.currencyValue,
            spendingRecords = regularSpending.spendingRecords.map {
                it.copy(
                    idSpendingSummary = idSpendingSummary,
                    idSpendingRecord = UUID.randomUUID(),
                )
            }
        )
    }

    private fun getExpectedActualizationDate(dto: RegularSpendingWithSpendingDataDto) =
        dto.lastActualizationDate
            .apply {
                return when (dto.periodUnit) {
                    PeriodUnit.Days -> this.plusDays(dto.actualizationPeriod.toLong())
                    PeriodUnit.Weeks -> this.plusDays(dto.actualizationPeriod.toLong() * 7)
                    PeriodUnit.Months -> this.plusMonths(dto.actualizationPeriod.toLong())
                }
            }
}

class RegularSpendingActualizationService(private val context: Context) {
    fun launchActualization() {
        val request = PeriodicWorkRequestBuilder<Actualizator>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(context).enqueue(request)
    }
}