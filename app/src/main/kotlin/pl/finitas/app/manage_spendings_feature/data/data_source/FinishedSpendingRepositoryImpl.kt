package pl.finitas.app.manage_spendings_feature.data.data_source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.data_source.dao.FinishedSpendingDao
import pl.finitas.app.core.data.data_source.dao.FinishedSpendingWithRecordFlat
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToSpendingRecord
import pl.finitas.app.core.data.model.relations.SpendingSummaryToFinishedSpending
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecordDto
import pl.finitas.app.manage_spendings_feature.domain.repository.FinishedSpendingRepository
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.time.LocalDateTime
import java.util.UUID

class FinishedSpendingRepositoryImpl(
    private val finishedSpendingDao: FinishedSpendingDao,
) : FinishedSpendingRepository {

    override fun getFinishedSpendings(idsUser: List<UUID>): Flow<List<FinishedSpendingWithRecordsDto>> {
        return finishedSpendingDao.getFinishedSpendingsWithRecordFlat(idsUser).map { it.toDto() }
    }

    override fun getFinishedSpendingsByIdUser(idUser: UUID): Flow<List<FinishedSpendingWithRecordsDto>> {
        return finishedSpendingDao.getFinishedSpendingsWithRecordByIdUserFlat(idUser).map { it.toDto() }
    }

    override suspend fun findFinishedSpendingWithRecordBy(idTotalSpending: UUID): FinishedSpendingWithRecordsDto? {
        TODO("Not yet implemented")
    }

    override suspend fun upsertFinishedSpendingWithRecords(finishedSpending: FinishedSpendingWithRecordsDto) {
        val generatedSpendingSummary = finishedSpending.idSpendingSummary

        finishedSpendingDao.upsertFinishedSpendingWithRecords(
            SpendingSummaryToFinishedSpending(
                spendingSummary = SpendingSummary(
                    idSpendingSummary = generatedSpendingSummary,
                    name = finishedSpending.title,
                    currencyValue = finishedSpending.currencyValue,
                ),
                finishedSpending = FinishedSpending(
                    idReceipt = finishedSpending.idReceipt,
                    purchaseDate = finishedSpending.purchaseDate,
                    idUser = null,
                    idSpendingSummary = generatedSpendingSummary,
                    isDeleted = false,
                )
            ),
            finishedSpending.spendingRecords.map {
                SpendingRecordDataToSpendingRecord(
                    spendingRecord = SpendingRecord(
                        idSpendingSummary = generatedSpendingSummary,
                        idSpendingRecordData = it.idSpendingRecord,
                        price = it.price,
                    ),
                    spendingRecordData = SpendingRecordData(
                        name = it.name,
                        idCategory = it.idCategory,
                        idSpendingRecordData = it.idSpendingRecord,
                    )
                )
            }
        )
    }

    override suspend fun deleteFinishedSpendingById(idFinishedSpending: UUID) {
        finishedSpendingDao.deleteWithRecords(idFinishedSpending)
    }

    override suspend fun markAsDeleted(idFinishedSpending: UUID) {
        finishedSpendingDao.markFinishedSpendingByIdAsDeleted(idFinishedSpending)
    }

    override suspend fun getById(idFinishedSpending: UUID): FinishedSpending {
        return finishedSpendingDao.findFinishedSpendingById(idFinishedSpending)
            ?: throw FinishedSpendingNotFoundException(idFinishedSpending)
    }
}

private fun List<FinishedSpendingWithRecordFlat>.toDto() =
    groupBy {
        TempFinishedSpending(
            idSpendingSummary = it.idSpendingSummary,
            title = it.title,
            purchaseDate = it.purchaseDate,
            isDeleted = it.isDeleted,
            idUser = it.idUser,
            currencyValue = it.currencyValue,
        )
    }.map { (tempFinishedSpending, spendingRecords) ->
        FinishedSpendingWithRecordsDto(
            idSpendingSummary = tempFinishedSpending.idSpendingSummary,
            title = tempFinishedSpending.title,
            purchaseDate = tempFinishedSpending.purchaseDate,
            currencyValue = tempFinishedSpending.currencyValue,
            isDeleted = tempFinishedSpending.isDeleted,
            idReceipt = null,
            idUser = tempFinishedSpending.idUser,
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

private data class TempFinishedSpending(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
    val isDeleted: Boolean,
    val idUser: UUID?,
    val currencyValue: CurrencyValue,
)

class FinishedSpendingNotFoundException(
    idFinishedSpending: UUID,
): Exception("Finished spending with id '$idFinishedSpending' not found")
