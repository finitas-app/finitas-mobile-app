package pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemCategoryView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListElement
import java.util.UUID


data class ShoppingListState(
    val title: String,
    val color: Int,
    val categories: List<ShoppingItemCategoryView>,
    val idShoppingList: UUID? = null,
) {
    val addSpending = getSpendingsMutator { list, spending -> list + spending }
    val removeSpending = getSpendingsMutator { list, spending -> list - spending }


    private fun getSpendingsMutator(action: (List<ShoppingListElement>, ShoppingListElement) -> List<ShoppingListElement>) =
        { spendingRecord: ShoppingItemView ->
            val indexOfCategory =
                categories.indexOfFirst { it.idSpendingCategory == spendingRecord.idSpendingCategory }
            val category = categories[indexOfCategory]
            val result = categories.toMutableList()
            result[indexOfCategory] =
                category.copy(elements = action(category.elements, spendingRecord))
            copy(categories = result)
        }

    companion object {
        val colors = listOf(
            Color(0xFFF85784),
            Color(0xFF76A470),
            Color(0xFF299182),
            Color(0xFFFBA776),
            Color(0xFFE1BC6A),
            Color(0xFF635B7D),
        )

        val emptyState = ShoppingListState("", colors[0].toArgb(), listOf())
    }
}
