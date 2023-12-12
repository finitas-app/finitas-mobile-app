package pl.finitas.app.manage_additional_elements.presentation.spending_category

import java.util.UUID

sealed interface SpendingCategoryEvent {
    data class AddSpendingCategoryEvent(val idParent: UUID?): SpendingCategoryEvent
    data class UpdateSpendingCategoryEvent(val idSpendingCategory: UUID): SpendingCategoryEvent
}