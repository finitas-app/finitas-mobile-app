package pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListService
import java.util.UUID

class ShoppingListViewModel(
    private val shoppingListService: ShoppingListService,
): ViewModel() {
    val shoppingLists = shoppingListService.getShoppingLists()
    
    var selected: UUID? by mutableStateOf<UUID?>(null)
        private set

    fun onDeleteShoppingList(idShoppingList: UUID) {
        viewModelScope.launch {
            if (idShoppingList == selected) selected = null
            shoppingListService.deleteShoppingListBy(idShoppingList)
        }
    }

    fun selectElement(idShoppingList: UUID) {
        selected = if (selected == idShoppingList) null else idShoppingList
    }
}