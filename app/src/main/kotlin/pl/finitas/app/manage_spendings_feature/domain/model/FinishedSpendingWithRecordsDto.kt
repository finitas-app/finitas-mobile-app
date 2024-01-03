package pl.finitas.app.manage_spendings_feature.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class FinishedSpendingWithRecordsDto(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
    val isDeleted: Boolean,
    val spendingRecords: List<SpendingRecordDto>,
)

data class SpendingRecordDto(
    val name: String,
    val price: BigDecimal,
    val idCategory: UUID,
    val idSpendingRecord: UUID? = null,
    val idSpendingSummary: UUID? = null,
)