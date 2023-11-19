package pl.finitas.app.manage_spendings_feature.domain.services

import java.math.BigDecimal
import java.time.LocalDateTime


sealed interface SpendingElement {
    val name: String
    val totalPrice: BigDecimal
}

data class SpendingContainer(
    override val name: String,
    val idCategory: Int,
    val spendingElements: List<SpendingElement>,
) : SpendingElement {
    override val totalPrice by lazy { spendingElements.sumOf { it.totalPrice } }
}

data class SpendingRecordView(
    override val name: String,
    override val totalPrice: BigDecimal,
    val idCategory: Int,
) : SpendingElement


data class TotalSpendingView(
    val idTotalSpending: Int,
    override val name: String,
    val date: LocalDateTime,
    val spendingElements: List<SpendingElement>,
) : SpendingElement {
    override val totalPrice by lazy { spendingElements.sumOf { it.totalPrice } }
}