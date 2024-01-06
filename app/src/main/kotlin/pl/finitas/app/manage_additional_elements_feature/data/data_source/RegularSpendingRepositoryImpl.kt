package pl.finitas.app.manage_additional_elements_feature.data.data_source

import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.data_source.dao.FinishedSpendingDao
import pl.finitas.app.core.data.data_source.dao.RegularSpendingDao
import pl.finitas.app.core.data.data_source.dao.RegularSpendingWithRecordFlat
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToSpendingRecord
import pl.finitas.app.core.data.model.relations.SpendingSummaryToFinishedSpending
import pl.finitas.app.core.data.model.relations.SpendingSummaryToRegularSpending
import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto
import pl.finitas.app.manage_additional_elements_feature.domain.SpendingRecordDto
import pl.finitas.app.manage_additional_elements_feature.domain.repositories.RegularSpendingRepository

class RegularSpendingRepositoryImpl(
    private val regularSpendingDao: RegularSpendingDao,
    private val finishedSpendingDao: FinishedSpendingDao,
    ) : RegularSpendingRepository {

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

    override fun getRegularSpendingsFlow() =
        regularSpendingDao
            .getRegularSpendingsWithRecordFlatFlow()
            .map(::mapEntitiesToDTOs)

    override suspend fun upsertFinishedSpendingWithRecords(totalSpending: FinishedSpendingWithRecordsDto) {
        val generatedSpendingSummary = totalSpending.idSpendingSummary

        finishedSpendingDao.upsertFinishedSpendingWithRecords(
            SpendingSummaryToFinishedSpending(
                spendingSummary = SpendingSummary(
                    idSpendingSummary = generatedSpendingSummary,
                    name = totalSpending.title,
                    currencyValue = totalSpending.currencyValue,
                ),
                finishedSpending = FinishedSpending(
                    idReceipt = null,
                    purchaseDate = totalSpending.purchaseDate,
                    idUser = null,
                    idSpendingSummary = generatedSpendingSummary,
                    isDeleted = false,
                )
            ),
            totalSpending.spendingRecords.map {
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

    private fun mapRegularSpendingDtoToSpendingSummaryToRegularSpending(regularSpendingDto: RegularSpendingWithSpendingDataDto) =
        SpendingSummaryToRegularSpending(
            spendingSummary = SpendingSummary(
                idSpendingSummary = regularSpendingDto.idSpendingSummary,
                name = regularSpendingDto.name,
                currencyValue = regularSpendingDto.currencyValue,
            ),
            RegularSpending(
                idSpendingSummary = regularSpendingDto.idSpendingSummary,
                actualizationPeriod = regularSpendingDto.actualizationPeriod,
                periodUnit = regularSpendingDto.periodUnit.ordinal,
                lastActualizationDate = regularSpendingDto.lastActualizationDate,
            )
        )

    private fun mapRegularSpendingDtoToSpendingRecordDataToSpendingRecordList(regularSpendingDto: RegularSpendingWithSpendingDataDto) =
        regularSpendingDto.spendingRecords.map {
            val generatedIdSpendingRecordData = it.idSpendingRecord
            SpendingRecordDataToSpendingRecord(
                spendingRecord = SpendingRecord(
                    idSpendingSummary = regularSpendingDto.idSpendingSummary,
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

    override suspend fun upsertRegularSpendingWithRecords(regularSpendingDto: RegularSpendingWithSpendingDataDto) {
        regularSpendingDao.upsertRegularSpendingWithRecords(
            mapRegularSpendingDtoToSpendingSummaryToRegularSpending(regularSpendingDto),
            mapRegularSpendingDtoToSpendingRecordDataToSpendingRecordList(regularSpendingDto)
        )
    }

    override suspend fun deleteRegularSpendingWithRecords(regularSpendingDto: RegularSpendingWithSpendingDataDto) {
        regularSpendingDao.deleteRegularSpendingWithRecords(
            mapRegularSpendingDtoToSpendingSummaryToRegularSpending(regularSpendingDto),
            mapRegularSpendingDtoToSpendingRecordDataToSpendingRecordList(regularSpendingDto)
        )
    }
}