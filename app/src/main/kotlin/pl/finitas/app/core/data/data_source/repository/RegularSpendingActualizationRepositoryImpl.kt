package pl.finitas.app.core.data.data_source.repository

import pl.finitas.app.core.data.data_source.dao.RegularSpendingDao
import pl.finitas.app.core.data.data_source.dao.RegularSpendingWithRecordFlat
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToSpendingRecord
import pl.finitas.app.core.data.model.relations.SpendingSummaryToFinishedSpending
import pl.finitas.app.core.domain.repository.RegularSpendingActualizationRepository
import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto
import pl.finitas.app.manage_additional_elements_feature.domain.SpendingRecordDto

class RegularSpendingActualizationRepositoryImpl(
    private val regularSpendingDao: RegularSpendingDao,
) : RegularSpendingActualizationRepository {

    private fun mapEntitiesToDTOs(entities: List<RegularSpendingWithRecordFlat>) =
        entities
            .groupBy { it.idSpendingSummary }
            .values
            .map {
                val firstFlattened = it.first()
                RegularSpendingWithSpendingDataDto(
                    idSpendingSummary = firstFlattened.idSpendingSummary,
                    name = firstFlattened.title,
                    actualizationPeriod = firstFlattened.actualizationPeriod,
                    periodUnit = PeriodUnit.entries[firstFlattened.periodUnit],
                    lastActualizationDate = firstFlattened.lastActualizationDate,
                    currencyValue = firstFlattened.currencyValue,
                    spendingRecords = it.map { spendingRecord ->
                        SpendingRecordDto(
                            idSpendingRecord = spendingRecord.idSpendingRecord,
                            idSpendingSummary = spendingRecord.idSpendingSummary,
                            name = spendingRecord.spendingRecordName,
                            price = spendingRecord.price,
                            idCategory = spendingRecord.idCategory,
                        )
                    }
                )
            }

    override suspend fun upsertFinishedSpendingAndRegularSpending(
        regularSpending: RegularSpendingWithSpendingDataDto,
        finishedSpending: FinishedSpendingWithRecordsDto,
    ) {
        val generatedSpendingSummary = finishedSpending.idSpendingSummary

        regularSpendingDao.upsertRegularSpendingWithFinishedSpendingWithRecords(
            RegularSpending(
                actualizationPeriod = regularSpending.actualizationPeriod,
                periodUnit = regularSpending.periodUnit.ordinal,
                lastActualizationDate = regularSpending.lastActualizationDate,
                idSpendingSummary = regularSpending.idSpendingSummary
            ),
            SpendingSummaryToFinishedSpending(
                spendingSummary = SpendingSummary(
                    idSpendingSummary = generatedSpendingSummary,
                    name = finishedSpending.title,
                    currencyValue = finishedSpending.currencyValue,
                ),
                finishedSpending = FinishedSpending(
                    idReceipt = null,
                    purchaseDate = finishedSpending.purchaseDate,
                    idUser = null,
                    idSpendingSummary = generatedSpendingSummary,
                    isDeleted = false,
                )
            ),
            finishedSpending.spendingRecords.map {
                val generatedIdSpendingRecordData = it.idSpendingRecord
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

    override suspend fun getRegularSpendings() =
        regularSpendingDao
            .getRegularSpendingsWithRecordFlat()
            .let(::mapEntitiesToDTOs)
}