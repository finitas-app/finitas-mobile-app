package pl.finitas.app.manage_additional_elements_feature.presentation.components

import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.NameableCollection
import java.math.BigDecimal
import java.util.UUID

sealed interface SpendingElementView : Nameable {
    val totalPrice: BigDecimal
}

data class SpendingCategoryView(
    override val name: String,
    override val elements: List<SpendingElementView>,
    val idCategory: UUID,
) : SpendingElementView, NameableCollection<SpendingElementView> {
    override val totalPrice: BigDecimal by lazy { elements.sumOf { it.totalPrice } }
}

data class SpendingRecordView(
    override val name: String,
    override val totalPrice: BigDecimal,
    val idSpendingRecord: UUID,
    val idCategory: UUID,
) : SpendingElementView
