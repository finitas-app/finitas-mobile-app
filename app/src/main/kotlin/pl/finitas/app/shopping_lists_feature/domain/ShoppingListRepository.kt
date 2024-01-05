package pl.finitas.app.shopping_lists_feature.domain

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.ShoppingList
import java.util.UUID

interface ShoppingListRepository {
    fun getShoppingLists(): Flow<List<ShoppingListDto>>

    suspend fun getShoppingListBy(idShoppingList: UUID): ShoppingListDto

    suspend fun upsertShoppingList(shoppingListDto: ShoppingListDto)

    suspend fun deleteShoppingListBy(idShoppingList: UUID)
    suspend fun markAsDeleted(idShoppingList: UUID)
    suspend fun getShoppingListVersionBy(idShoppingList: UUID): ShoppingList
}