package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable

@Serializable
data class DeleteFinishedSpendingRequest(
    val idSpendingSummary: String,
    val idUser: String,
)

@Serializable
class FinishedSpendingDto(
    val spendingSummary: SpendingSummaryDto,
    val idReceipt: String?,
    val purchaseDate: Int,
    val version: Int,
    val idUser: String,
    val isDeleted: Boolean,
)

@Serializable
data class SpendingSummaryDto(
    val idSpendingSummary: String,
    val createdAt: Int,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
)

@Serializable
data class SpendingRecordDto(
    val idSpendingRecord: String,
    val spendingRecordData: SpendingRecordDataDto,
)