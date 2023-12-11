package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryView
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingElement
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingRecordView
import java.time.LocalDate
import java.util.UUID

data class TotalSpendingState(
    val title: String,
    val date: LocalDate,
    val categories: List<SpendingCategoryView>,
    val idTotalSpending: UUID? = null,
) {
    val addSpending = getSpendingsMutator { list, spending -> list + spending }
    val removeSpending = getSpendingsMutator { list, spending -> list - spending }


    private fun getSpendingsMutator(action: (List<SpendingElement>, SpendingRecordView) -> List<SpendingElement>) =
        { spendingRecord: SpendingRecordView ->
            val indexOfCategory =
                categories.indexOfFirst { it.idCategory == spendingRecord.idCategory }
            val category = categories[indexOfCategory]
            val result = categories.toMutableList()
            result[indexOfCategory] =
                category.copy(spendingElements = action(category.spendingElements, spendingRecord))
            copy(categories = result)
        }

    companion object {
        val emptyState = TotalSpendingState("", LocalDate.now(), listOf())
    }
}