package pl.finitas.app.shopping_lists_feature.domain

import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.NameableCollection
import java.util.UUID

sealed interface ShoppingListElement : Nameable

data class ShoppingItemView(
    override val name: String,
    val amount: Int,
    val idSpendingCategory: UUID,
    val idShoppingItem: UUID,
): ShoppingListElement

data class ShoppingItemCategoryView(
    override val name: String,
    override val elements: List<ShoppingListElement>,
    val idSpendingCategory: UUID,
) : ShoppingListElement, NameableCollection<ShoppingListElement>

data class ShoppingListView(
    override val name: String,
    override val elements: List<ShoppingListElement>,
    val color: Int,
    val idShoppingList: UUID,
    val idUser: UUID? = null,
) : ShoppingListElement, NameableCollection<ShoppingListElement>

data class ShoppingListDto(
    val name: String,
    val color: Int,
    val idShoppingList: UUID,
    val shoppingItems: List<ShoppingItemDto>,
    val idUser: UUID? = null,
)

data class ShoppingItemDto(
    val idSpendingRecordData: UUID,
    val name: String,
    val amount: Int,
    val idSpendingCategory: UUID,
)