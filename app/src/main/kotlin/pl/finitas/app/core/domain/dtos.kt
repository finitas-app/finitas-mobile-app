package pl.finitas.app.core.domain

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

interface Nameable {
    val name: String
}

interface NameableCollection: Nameable {
    val elements: List<Nameable>
}



data class ShoppingItemView(
    override val name: String,
    val idSpendingCategory: UUID,
    val idSpendingList: UUID,
) : Nameable