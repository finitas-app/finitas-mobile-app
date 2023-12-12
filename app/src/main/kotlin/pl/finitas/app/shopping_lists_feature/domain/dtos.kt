package pl.finitas.app.shopping_lists_feature.domain

import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.NameableCollection
import java.util.UUID

sealed interface ShoppingListElement : Nameable

data class ShoppingItem(
    override val name: String,
    val amount: Int,
): ShoppingListElement

data class ShoppingListView(
    override val name: String,
    override val elements: List<ShoppingListElement>,
    val idShoppingList: UUID,
    val idUser: UUID?,
) : ShoppingListElement, NameableCollection

data class ShoppingItemCategory(
    override val name: String,
    override val elements: List<ShoppingListElement>,
    val idSpendingCategory: UUID,
    val idShoppingList: UUID,
) : ShoppingListElement, NameableCollection
