package pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemCategoryView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListService
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListView

class UpsertShoppingListViewModel(
    private val shoppingListService: ShoppingListService,
    private val spendingCategoryService: SpendingCategoryService,
) : ViewModel() {

    var isDialogOpen by mutableStateOf(false)
        private set

    var shoppingListState by mutableStateOf(ShoppingListState.emptyState)
        private set

    var titleErrors by mutableStateOf<List<String>?>(null)
        private set

    var errors by mutableStateOf<List<String>?>(null)
        private set

    fun openDialog(shoppingList: ShoppingListView?) {
        viewModelScope.launch {
            val categories = spendingCategoryService
                .getSpendingCategoriesByIdUserFlat(shoppingList?.idUser)
                .map { it.toShoppingItemCategory() }
            shoppingListState = if (shoppingList == null) {
                shoppingListState.copy(categories = categories)
            } else {
                shoppingListService.getShoppingListBy(shoppingList.idShoppingList, categories)
            }
            println(shoppingList)
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
        viewModelScope.launch {
            try {
                shoppingListService.upsertShoppingList(shoppingListState)
                closeDialog()
            } catch (e: InputValidationException) {
                titleErrors = e.errors["title"]
                errors = e.errors[null]
            } catch (e: Exception) {
                errors = listOf("There's been a fatal error.")
            }
        }
    }
}

private fun SpendingCategoryView.toShoppingItemCategory() = ShoppingItemCategoryView(
    name = name,
    elements = listOf(),
    idSpendingCategory = idCategory,
)