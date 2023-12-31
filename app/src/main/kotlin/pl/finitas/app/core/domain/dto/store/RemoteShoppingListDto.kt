package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID
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
    val idShoppingList: SerializableUUID,
    val shoppingItems: List<RemoteShoppingItemDto>,
    val name: String,
    val color: Int,
    override val version: Int,
    override val idUser: SerializableUUID,
    override val isDeleted: Boolean,
    val isFinished: Boolean,
): SynchronizableEntity

@Serializable
data class RemoteShoppingItemDto(
    val idShoppingItem: SerializableUUID,
    val amount: Int,
    val idSpendingRecordData: SerializableUUID,
    val name: String,
    val idCategory: SerializableUUID,
)