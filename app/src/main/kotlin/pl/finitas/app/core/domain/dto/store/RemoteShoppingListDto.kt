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
    val version: Int,
    @Serializable(UUIDSerializer::class)
    val name: String,
    val idUser: SerializableUUID?,
    val isDeleted: Boolean,
    val isFinished: Boolean,
    val color: Int,
)

@Serializable
data class RemoteShoppingItemDto(
    @Serializable(UUIDSerializer::class)
    val idShoppingItem: UUID,
    val amount: Int,
    val spendingRecordData: RemoteSpendingRecordDataDto,
)