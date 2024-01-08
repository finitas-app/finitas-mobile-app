package pl.finitas.app.manage_additional_elements_feature.domain

import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.validateBuilder
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

enum class PeriodUnit {
    Months,
    Weeks,
    Days,
}

data class FinishedSpendingWithRecordsDto(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
    val currencyValue: CurrencyValue,
    val spendingRecords: List<SpendingRecordDto>,
)

data class RegularSpendingWithSpendingDataDto (
    val idSpendingSummary: UUID,
    override val name: String,
    val lastActualizationDate: LocalDateTime,
    val actualizationPeriod: Int,
    val periodUnit: PeriodUnit,
    val currencyValue: CurrencyValue,
    val spendingRecords: List<SpendingRecordDto>,
) : Nameable {
    init {
        validateBuilder {
            validate(name.isNotBlank(), "title") { "Title cannot be blank." }
            validate(actualizationPeriod > 0, "actualizationPeriod") { "Actualization period should be a positive number." }
            validate(spendingRecords.isNotEmpty()) { "Regular spending should have at least 1 product." }
        }
    }
}

data class SpendingRecordDto(
    val name: String,
    val price: BigDecimal,
    val idCategory: UUID,
    val idSpendingSummary: UUID,
    val idSpendingRecord: UUID,
)
