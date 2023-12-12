package pl.finitas.app.manage_additional_elements.presentation.spending_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.domain.services.UpsertSpendingCategoryCommand

class SpendingCategoryViewModel(
    private val spendingCategoryService: SpendingCategoryService,
) : ViewModel() {
    val categories = spendingCategoryService.getSpendingCategoriesFlow()

    var spendingCategoryState by mutableStateOf(SpendingCategoryState(""))
        private set

    var isUpsertCategoryDialogOpen by mutableStateOf(false)
        private set

    fun openUpsertCategoryDialog(spendingCategoryEvent: SpendingCategoryEvent) {
        when (spendingCategoryEvent) {
            is SpendingCategoryEvent.AddSpendingCategoryEvent -> {
                spendingCategoryState = SpendingCategoryState("", spendingCategoryEvent.idParent)
            }

            is SpendingCategoryEvent.UpdateSpendingCategoryEvent -> {
                viewModelScope.launch {
                    spendingCategoryState = spendingCategoryService
                        .getSpendingCategoryById(spendingCategoryEvent.idSpendingCategory)
                        .copy()
                }
            }
        }
        isUpsertCategoryDialogOpen = true
    }

    fun closeUpsertCategoryDialog() {
        spendingCategoryState = SpendingCategoryState("")
        isUpsertCategoryDialogOpen = false
    }

    fun setCategoryName(name: String) {
        spendingCategoryState = spendingCategoryState.copy(name = name)
    }

    fun save() {
        viewModelScope.launch {
            spendingCategoryService.upsertSpendingCategory(
                UpsertSpendingCategoryCommand(
                    name = spendingCategoryState.name,
                    idSpendingCategory = spendingCategoryState.idCategory,
                    idParentCategory = spendingCategoryState.idParentCategory,
                )
            )
        }
    }
}