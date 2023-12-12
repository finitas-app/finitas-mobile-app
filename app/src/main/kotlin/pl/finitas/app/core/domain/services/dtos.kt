package pl.finitas.app.core.domain.services

import java.util.UUID

data class UpsertSpendingCategoryCommand(
    val idSpendingCategory: UUID?,
    val idParentCategory: UUID?,
    val name: String,
)