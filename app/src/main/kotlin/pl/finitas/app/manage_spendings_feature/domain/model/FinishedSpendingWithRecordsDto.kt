package pl.finitas.app.manage_spendings_feature.domain.model

import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class FinishedSpendingWithRecordsDto(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
    val currencyValue: CurrencyValue,
    val isDeleted: Boolean,
    val idReceipt: UUID?,
    val idUser: UUID?,
    val spendingRecords: List<SpendingRecordDto>,
)

data class SpendingRecordDto(
    val name: String,
    val price: BigDecimal,
    val idCategory: UUID,
    val idSpendingRecord: UUID,
    val idSpendingSummary: UUID? = null,
)