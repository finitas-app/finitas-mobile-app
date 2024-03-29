package pl.finitas.app.sync_feature.data.data_source

import pl.finitas.app.core.data.data_source.dao.FinishedSpendingDao
import pl.finitas.app.core.data.data_source.dao.UserDao
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToSpendingRecord
import pl.finitas.app.core.data.model.relations.SpendingSummaryToFinishedSpending
import pl.finitas.app.core.domain.repository.FinishedSpendingVersion
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecordDto
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import pl.finitas.app.sync_feature.domain.repository.FinishedSpendingSyncRepository
import pl.finitas.app.sync_feature.domain.repository.SyncFinishedSpendingWithRecordsDto
import java.time.LocalDateTime
import java.util.UUID

class FinishedSpendingSyncRepositoryImpl(
    private val finishedSpendingDao: FinishedSpendingDao,
    private val userDao: UserDao,
): FinishedSpendingSyncRepository {
    override suspend fun getChangedFinishedSpendings(): List<FinishedSpendingWithRecordsDto> {
        return finishedSpendingDao.getChangedFinishedSpendings().let { finishedRecordWithSpendingFlat ->
            finishedRecordWithSpendingFlat
                .groupBy {
                    TempFinishedSpending(
                        idSpendingSummary = it.idSpendingSummary,
                        title = it.title,
                        purchaseDate = it.purchaseDate,
                        isDeleted = it.isDeleted,
                        currencyValue = it.currencyValue,
                    )
                }
                .map { (tempFinishedSpending, spendingRecords) ->
                    FinishedSpendingWithRecordsDto(
                        idSpendingSummary = tempFinishedSpending.idSpendingSummary,
                        title = tempFinishedSpending.title,
                        purchaseDate = tempFinishedSpending.purchaseDate,
                        isDeleted = tempFinishedSpending.isDeleted,
                        idReceipt = null,
                        idUser = null,
                        currencyValue = tempFinishedSpending.currencyValue,
                        spendingRecords = spendingRecords.map {
                            SpendingRecordDto(
                                name = it.spendingRecordName,
                                price = it.price,
                                idCategory = it.idCategory,
                                idSpendingRecord = it.idSpendingRecord,
                                idSpendingSummary = it.idSpendingSummary,
                            )
                        }
                    )
                }
        }
    }

    override suspend fun upsertFinishedSpendingWithRecords(finishedSpendings: List<SyncFinishedSpendingWithRecordsDto>) {
        finishedSpendings.map { finishedSpending ->
            val generatedSpendingSummary = finishedSpending.idSpendingSummary

            finishedSpendingDao.upsertFinishedSpendingWithRecords(
                SpendingSummaryToFinishedSpending(
                    spendingSummary = SpendingSummary(
                        idSpendingSummary = generatedSpendingSummary,
                        name = finishedSpending.title,
                        currencyValue = finishedSpending.currencyValue,
                    ),
                    finishedSpending = FinishedSpending(
                        idReceipt = null,
                        purchaseDate = finishedSpending.purchaseDate,
                        idUser = finishedSpending.idUser,
                        idSpendingSummary = generatedSpendingSummary,
                        isDeleted = finishedSpending.isDeleted,
                        version = finishedSpending.version,
                    )
                ),
                finishedSpending.spendingRecords.map {
                    val generatedIdSpendingRecordData = it.idSpendingRecord ?: UUID.randomUUID()
                    SpendingRecordDataToSpendingRecord(
                        spendingRecord = SpendingRecord(
                            idSpendingSummary = generatedSpendingSummary,
                            idSpendingRecordData = generatedIdSpendingRecordData,
                            price = it.price,
                        ),
                        spendingRecordData = SpendingRecordData(
                            name = it.name,
                            idCategory = it.idCategory,
                            idSpendingRecordData = generatedIdSpendingRecordData,
                        )
                    )
                }
            )
        }
    }

    override suspend fun deleteMarkedAndSynchronizedFinishedSpendings() {
        finishedSpendingDao.deleteMarkedFinishedSpendingsAndSynchronized()
    }

    override suspend fun setFinishedSpendingVersions(finishedSpendingVersions: List<FinishedSpendingVersion>) {
        finishedSpendingVersions.forEach {
            userDao.setFinishedSpendingVersion(it.idUser, it.version)
        }
    }

    override suspend fun deleteByIdUser(idUser: UUID) {
        finishedSpendingDao.deleteByIdUser(idUser)
    }
}

private data class TempFinishedSpending(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
    val isDeleted: Boolean,
    val currencyValue: CurrencyValue,
)
