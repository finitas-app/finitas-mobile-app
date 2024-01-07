package pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListService
import java.util.UUID

class ShoppingListViewModel(
    private val shoppingListService: ShoppingListService,
): ViewModel() {
    var deleteErrors by mutableStateOf<List<String>?>(null)

    val shoppingLists = shoppingListService.getShoppingLists().onEach { lists ->
        if (selected !in lists.map { it.idShoppingList }) {
            selected = null
        }
    }
    
    var selected by mutableStateOf<UUID?>(null)
        private set

    fun onDeleteShoppingList(idShoppingList: UUID) {
        viewModelScope.launch {
            try {
                shoppingListService.deleteShoppingListBy(idShoppingList)
            } catch (e: InputValidationException) {
                deleteErrors = e.errors[null]
            }
        }
    }

    fun selectElement(idShoppingList: UUID) {
        selected = if (selected == idShoppingList) null else idShoppingList
    }
}