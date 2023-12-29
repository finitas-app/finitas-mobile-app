package pl.finitas.app.room_feature.domain.service

import pl.finitas.app.room_feature.domain.repository.RoomShoppingListRepository
import java.util.UUID

class RoomShoppingListService(
    private val roomShoppingListRepository: RoomShoppingListRepository,
) {
    fun getShoppingListsPreview() = roomShoppingListRepository.getShoppingListsPreview()

    fun getShoppingListsBy(idsShoppingList: List<UUID>) = roomShoppingListRepository.getShoppingListsBy(idsShoppingList)
}
