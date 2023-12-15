package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable

@Serializable
data class DeleteShoppingListRequest(
    val idShoppingList: String,
    val idUser: String,
)

@Serializable
class ShoppingListDto(
    val idShoppingList: String,
    val shoppingItems: List<ShoppingItemDto>,
    val version: Int,
    val idUser: String,
    val isDeleted: Boolean,
)

@Serializable
data class ShoppingItemDto(
    val idShoppingItem: String,
    val isDone: Int,
    val spendingRecordData: SpendingRecordDataDto,
)