package pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemCategoryView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListService
import java.util.UUID

class UpsertShoppingListViewModel(
    private val shoppingListService: ShoppingListService,
    private val spendingCategoryService: SpendingCategoryService,
) : ViewModel() {

    var isDialogOpen by mutableStateOf(false)
        private set

    var shoppingListState by mutableStateOf(ShoppingListState.emptyState)
        private set

    fun openDialog(idShoppingList: UUID?) {
        viewModelScope.launch {
            val categories = spendingCategoryService
                .getSpendingCategoriesFlat()
                .map { it.toShoppingItemCategory() }
            shoppingListState = if (idShoppingList == null) {
                shoppingListState.copy(categories = categories)
            } else {
                shoppingListService.getShoppingListBy(idShoppingList, categories)
            }

            isDialogOpen = true
        }
    }

    fun setTitle(title: String) {
        shoppingListState = shoppingListState.copy(title = title)
    }

    fun setColor(color: Int) {
        println("color $shoppingListState")
        shoppingListState = shoppingListState.copy(color = color)
    }

    fun addSpending(shoppingItemView: ShoppingItemView) {
        shoppingListState = shoppingListState.addSpending(shoppingItemView)
    }

    fun removeSpending(shoppingItemView: ShoppingItemView) {
        shoppingListState = shoppingListState.removeSpending(shoppingItemView)
    }

    fun closeDialog() {
        shoppingListState = ShoppingListState.emptyState
        isDialogOpen = false
    }

    fun onSave() {
        if (shoppingListState.categories.flatMap { it.elements }.isNotEmpty()) {
            viewModelScope.launch {
                shoppingListService.upsertShoppingList(shoppingListState)
                closeDialog()
            }
        }
    }
}

private fun SpendingCategoryView.toShoppingItemCategory() = ShoppingItemCategoryView(
    name = name,
    elements = listOf(),
    idSpendingCategory = idCategory,
)