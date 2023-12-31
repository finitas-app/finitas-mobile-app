package pl.finitas.app.room_feature.domain.service

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.room_feature.domain.repository.RoomShoppingListRepository
import java.util.UUID

class RoomShoppingListService(
    private val roomShoppingListRepository: RoomShoppingListRepository,
) {
    fun getShoppingListsPreview() = roomShoppingListRepository.getShoppingListsPreview()

    fun getShoppingListsBy(idsShoppingList: List<UUID>): Flow<List<ShoppingList>> {
        return roomShoppingListRepository.getShoppingListsBy(idsShoppingList)
    }
}
