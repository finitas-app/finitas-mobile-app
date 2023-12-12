package pl.finitas.app.manage_additional_elements.presentation.spending_category

import java.util.UUID

data class SpendingCategoryState(
    val name: String,
    val idParentCategory: UUID? = null,
    val idCategory: UUID? = null,
)
