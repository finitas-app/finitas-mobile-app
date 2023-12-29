package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.UUIDSerializer
import java.util.UUID

@Serializable
data class DeleteShoppingListRequest(
    @Serializable(UUIDSerializer::class)
    val idShoppingList: UUID,
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
)

@Serializable
class RemoteShoppingListDto(
    @Serializable(UUIDSerializer::class)
    val idShoppingList: UUID,
    val shoppingItems: List<ShoppingItemDto>,
    val version: Int,
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val isDeleted: Boolean,
)

@Serializable
data class ShoppingItemDto(
    @Serializable(UUIDSerializer::class)
    val idShoppingItem: UUID,
    val isDone: Int,
    val spendingRecordData: SpendingRecordDataDto,
)