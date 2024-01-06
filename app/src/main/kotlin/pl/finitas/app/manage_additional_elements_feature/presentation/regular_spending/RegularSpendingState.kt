package pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending

import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_additional_elements_feature.domain.PeriodUnit
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.time.LocalDateTime
import java.util.UUID

data class RegularSpendingState(
    val title: String = "",
    val idSpendingSummary: UUID? = null,
    val periodUnit: PeriodUnit = PeriodUnit.Months,
    val lastActualizationDate: LocalDateTime = LocalDateTime.now(),
    val actualizationPeriod: UInt = 1.toUInt(),
    val currencyValue: CurrencyValue = CurrencyValue.PLN,
    val categories: List<SpendingCategoryView> = arrayListOf(),
) {
    val addSpending = getSpendingsMutator { list, spending -> list + spending }
    val removeSpending = getSpendingsMutator { list, spending -> list - spending }

    private fun getSpendingsMutator(
        action: (List<SpendingElementView>, SpendingRecordView) -> List<SpendingElementView>
    ) = { spendingRecord: SpendingRecordView ->
            val indexOfCategory =
                categories.indexOfFirst { it.idCategory == spendingRecord.idCategory }
            val category = categories[indexOfCategory]
            val result = categories.toMutableList()
            result[indexOfCategory] =
                category.copy(elements = action(category.elements, spendingRecord))
            copy(categories = result)
        }
}
