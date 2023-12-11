package pl.finitas.app.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.data_source.dao.FinishedSpendingDao
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToSpendingRecord
import pl.finitas.app.core.data.model.relations.SpendingSummaryToFinishedSpending
import pl.finitas.app.core.domain.repository.TotalSpendingRepository
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecordDto
import java.time.LocalDateTime
import java.util.UUID

class FinishedSpendingRepositoryImpl(
    private val finishedSpendingDao: FinishedSpendingDao,
) : TotalSpendingRepository {

    override fun getFinishedSpendings(): Flow<List<FinishedSpendingWithRecordsDto>> {
        return finishedSpendingDao.getFinishedSpendingsWithRecordFlat().map { finishedRecordWithSpendingFlat ->
            finishedRecordWithSpendingFlat
                .groupBy {
                    TempFinishedSpending(
                        idSpendingSummary = it.idSpendingSummary,
                        title = it.title,
                        purchaseDate = it.purchaseDate,
                    )
                }
                .map { (tempFinishedSpending, spendingRecords) ->
                    FinishedSpendingWithRecordsDto(
                        tempFinishedSpending.idSpendingSummary,
                        tempFinishedSpending.title,
                        tempFinishedSpending.purchaseDate,
                        spendingRecords.map {
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

    override suspend fun findFinishedSpendingWithRecordBy(idTotalSpending: UUID): FinishedSpendingWithRecordsDto? {
        TODO("Not yet implemented")
    }

    override suspend fun upsertFinishedSpendingWithRecords(totalSpending: FinishedSpendingWithRecordsDto) {
        val generatedSpendingSummary = totalSpending.idSpendingSummary

        finishedSpendingDao.upsertFinishedSpendingWithRecords(
            SpendingSummaryToFinishedSpending(
                spendingSummary = SpendingSummary(
                    idSpendingSummary = generatedSpendingSummary,
                    name = totalSpending.title
                ),
                finishedSpending = FinishedSpending(
                    idReceipt = null,
                    purchaseDate = totalSpending.purchaseDate,
                    idUser = null,
                    idSpendingSummary = generatedSpendingSummary,
                )
            ),
            totalSpending.spendingRecords.map {
                val generatedIdSpendingRecordData = it.idSpendingRecord ?: UUID.randomUUID()
                SpendingRecordDataToSpendingRecord(
                    spendingRecord = SpendingRecord(
                        idSpendingSummary = generatedSpendingSummary,
                        idSpendingRecordData = generatedIdSpendingRecordData,
                    ),
                    spendingRecordData = SpendingRecordData(
                        name = it.name,
                        price = it.price,
                        idCategory = it.idCategory,
                        idSpendingRecordData = generatedIdSpendingRecordData,
                    )
                )
            }
        )
    }

    override suspend fun deleteFinishedSpending(totalSpending: FinishedSpending) {
        TODO("Not yet implemented")
    }


}

private data class TempFinishedSpending(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
)