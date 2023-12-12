package pl.finitas.app.core.domain.services

import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.NameableCollection
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class UpsertSpendingCategoryCommand(
    val idSpendingCategory: UUID?,
    val idParentCategory: UUID?,
    val name: String,
)

sealed interface SpendingElementView: Nameable {
    val totalPrice: BigDecimal
}

data class SpendingCategoryView(
    override val name: String,
    override val elements: List<SpendingElementView>,
    val idCategory: UUID,
) : SpendingElementView, NameableCollection {
    override val totalPrice: BigDecimal by lazy { elements.sumOf { it.totalPrice } }
}

data class SpendingRecordView(
    override val name: String,
    override val totalPrice: BigDecimal,
    val idSpendingRecord: UUID,
    val idCategory: UUID,
) : SpendingElementView


data class TotalViewSpendingView(
    override val name: String,
    override val elements: List<SpendingElementView>,
    val idTotalSpending: UUID,
    val date: LocalDateTime,
) : SpendingElementView, NameableCollection {
    override val totalPrice by lazy { elements.sumOf { it.totalPrice } }
}