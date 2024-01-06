package pl.finitas.app.core.domain.services

import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.NameableCollection
import pl.finitas.app.profile_feature.presentation.CurrencyValue
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
    val currency: CurrencyValue
}

data class SpendingCategoryView(
    override val name: String,
    override val elements: List<SpendingElementView>,
    val idCategory: UUID,
) : SpendingElementView, NameableCollection<SpendingElementView> {
    override val totalPrice: BigDecimal by lazy { elements.sumOf { it.totalPrice } }
    override val currency: CurrencyValue
        get() = elements.firstOrNull()?.currency ?: CurrencyValue.PLN
}

data class SpendingRecordView(
    override val name: String,
    override val totalPrice: BigDecimal,
    override val currency: CurrencyValue,
    val idSpendingRecord: UUID,
    val idCategory: UUID,
) : SpendingElementView


data class FinishedSpendingView(
    override val name: String,
    override val elements: List<SpendingElementView>,
    override val currency: CurrencyValue,
    val idFinishedSpending: UUID,
    val date: LocalDateTime,
    val idUser: UUID?,
) : SpendingElementView, NameableCollection<SpendingElementView> {
    override val totalPrice by lazy { elements.sumOf { it.totalPrice } }
}