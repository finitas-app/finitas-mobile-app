package pl.finitas.app.room_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.ShoppingList
import java.util.UUID

interface RoomShoppingListRepository {
    fun getShoppingListsBy(idsShoppingList: List<UUID>): Flow<List<ShoppingList>>
    fun getShoppingListsPreview(): Flow<List<ShoppingList>>
}