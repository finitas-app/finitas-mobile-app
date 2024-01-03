package pl.finitas.app.sync_feature.domain.repository

import pl.finitas.app.core.domain.repository.FinishedSpendingVersion
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

interface FinishedSpendingSyncRepository {

    suspend fun getChangedFinishedSpendings(): List<FinishedSpendingWithRecordsDto>


    suspend fun upsertFinishedSpendingWithRecords(finishedSpendings: List<SyncFinishedSpendingWithRecordsDto>)

    suspend fun deleteMarkedAndSynchronizedFinishedSpendings()

    suspend fun setFinishedSpendingVersions(finishedSpendingVersions: List<FinishedSpendingVersion>)
}



data class SyncFinishedSpendingWithRecordsDto(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
    val isDeleted: Boolean,
    val idUser: UUID,
    val version: Int,
    val spendingRecords: List<SyncSpendingRecordDto>,
)

data class SyncSpendingRecordDto(
    val name: String,
    val price: BigDecimal,
    val idCategory: UUID,
    val idSpendingRecord: UUID? = null,
    val idSpendingSummary: UUID? = null,
)
