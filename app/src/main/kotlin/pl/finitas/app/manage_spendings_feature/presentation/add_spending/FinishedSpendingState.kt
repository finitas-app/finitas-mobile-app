package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.domain.services.SpendingRecordView
import java.time.LocalDate
import java.util.UUID

data class FinishedSpendingState(
    val title: String,
    val date: LocalDate,
    val categories: List<SpendingCategoryView>,
    val idUser: UUID?,
    val idFinishedSpending: UUID? = null,
) {
    constructor(categories: List<SpendingCategoryView>, idUser: UUID?) : this("", LocalDate.now(), categories, idUser)
    constructor(
        categories: List<SpendingCategoryView>,
        finishedSpendingView: FinishedSpendingView,
    ) : this(
        title = finishedSpendingView.name,
        date = finishedSpendingView.date.toLocalDate(),
        categories = finishedSpendingView.elements.flatten().let { finishedSpendingCategories ->
            val associatedById = finishedSpendingCategories.groupBy { it.idCategory }

            categories.map { category ->
                category.copy(
                    elements = associatedById[category.idCategory]
                        ?.flatMap { it.elements }
                        ?.filterIsInstance<SpendingRecordView>()
                        ?: listOf()
                )
            }
        },
        idFinishedSpending = finishedSpendingView.idFinishedSpending,
        idUser = finishedSpendingView.idUser,
    )

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
        val emptyState = FinishedSpendingState("", LocalDate.now(), listOf(), null)
    }
}

private fun List<SpendingElementView>.flatten(): List<SpendingCategoryView> {
    return flatMap {
        if (it is SpendingCategoryView) {
            listOf(it) + it.elements.flatten()
        } else {
            listOf()
        }
    }
}

