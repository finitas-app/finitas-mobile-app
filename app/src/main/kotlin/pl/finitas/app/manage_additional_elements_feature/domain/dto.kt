package pl.finitas.app.manage_additional_elements_feature.domain

import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.exceptions.InputValidationException
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
    val spendingRecords: List<SpendingRecordDto>,
)

data class RegularSpendingWithSpendingDataDto(
    val idSpendingSummary: UUID,
    override val name: String,
    val lastActualizationDate: LocalDateTime,
    val actualizationPeriod: Int,
    val periodUnit: PeriodUnit,
    val spendingRecords: List<SpendingRecordDto>,
) : Nameable {
    init {
        listOfNotNull(
            if (name.isBlank()) "Name cannot be blank." else null,
            if (actualizationPeriod < 1) "Actualization period should be a positive number."
            else null,
            if (spendingRecords.isEmpty()) "Regular spending should have at least 1 product."
            else null,
        )
            .let {
                if (it.isNotEmpty()) throw InputValidationException(it)
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
