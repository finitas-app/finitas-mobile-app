package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.domain.services.SpendingRecordView
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


    private fun getSpendingsMutator(action: (List<SpendingElementView>, SpendingRecordView) -> List<SpendingElementView>) =
        { spendingRecord: SpendingRecordView ->
            val indexOfCategory =
                categories.indexOfFirst { it.idCategory == spendingRecord.idCategory }
            val category = categories[indexOfCategory]
            val result = categories.toMutableList()
            result[indexOfCategory] =
                category.copy(elements = action(category.elements, spendingRecord))
            copy(categories = result)
        }

    companion object {
        val emptyState = TotalSpendingState("", LocalDate.now(), listOf())
    }
}