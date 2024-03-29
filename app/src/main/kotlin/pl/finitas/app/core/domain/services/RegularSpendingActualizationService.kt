package pl.finitas.app.core.domain.services

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.finitas.app.core.data.WorkerProcessTags
import pl.finitas.app.core.domain.dto.store.RemoteFinishedSpendingDto
import pl.finitas.app.core.domain.dto.store.RemoteSpendingRecordDto
import pl.finitas.app.core.domain.repository.FinishedSpendingStoreRepository
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.RegularSpendingActualizationRepository
import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import java.util.concurrent.TimeUnit

class Actualizator(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {

    private val repository: RegularSpendingActualizationRepository by inject()
    private val finishedSpendingStoreRepository: FinishedSpendingStoreRepository by inject()
    private val profileRepository: ProfileRepository by inject()

    @WorkerThread
    override fun doWork(): Result {
        runBlocking {
            println("Regular spendings actualization start")
            repository.getRegularSpendings()
                .apply {
                    println("Successfully retrieved regular spendings: ${this.size}")
                }
                .filter {
                    LocalDate.now() >= getExpectedActualizationDate(it).toLocalDate()
                }
                .map {
                    println(
                        "Creating new finished spending. Name - ${it.name}, id - ${it.idSpendingSummary}"
                    )
                    val finishedSpendingDto = mapRegularSpendingToFinishedSpending(it)
                    repository.upsertFinishedSpendingAndRegularSpending(
                        regularSpending = it.copy(lastActualizationDate = LocalDateTime.now()),
                        finishedSpending = finishedSpendingDto
                    )
                    finishedSpendingDto
                }
                .let { spendings ->
                    val authorizedUser = profileRepository.getAuthorizedUserId().first()
                    if (authorizedUser != null) {
                        try {
                            finishedSpendingStoreRepository.changeFinishedSpendings(
                                spendings.map {
                                    it.toRemote(authorizedUser)
                                }
                            )
                        } catch (_: Exception) {

                        }
                    }
                }

        }

        return Result.success()
    }

    private fun mapRegularSpendingToFinishedSpending(
        regularSpending: RegularSpendingWithSpendingDataDto,
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
    private fun calculateDelay(desiredTime: LocalTime): Duration {
        val duration = Duration.between(LocalTime.now(), desiredTime)
        return if (duration.isNegative) duration.plusDays(1) else duration
    }

    fun launchActualization(time: LocalTime) {
        val workerManager = WorkManager.getInstance(context)
        workerManager.cancelAllWorkByTag(WorkerProcessTags.REGULAR_SPENDING_ACTUALIZATION.toString())
        val request = PeriodicWorkRequestBuilder<Actualizator>(1, TimeUnit.DAYS)
            .addTag(WorkerProcessTags.REGULAR_SPENDING_ACTUALIZATION.toString())
            .setInitialDelay(
                calculateDelay(time)
                    .also {
                        println(
                            "First regular spending update will occur in $it"
                        )
                    }
            )
            .build()
        workerManager.enqueue(request)
    }
}

private fun FinishedSpendingWithRecordsDto.toRemote(idUser: UUID): RemoteFinishedSpendingDto {
    return RemoteFinishedSpendingDto(
        idSpendingSummary = idSpendingSummary,
        idReceipt = null,
        purchaseDate = purchaseDate,
        version = 0,
        idUser = idUser,
        isDeleted = false,
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