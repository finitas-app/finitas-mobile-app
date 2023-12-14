package pl.finitas.app.shopping_lists_feature.domain

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ShoppingListRepository {
    fun getShoppingLists(): Flow<List<ShoppingListDto>>

    suspend fun getShoppingListBy(idShoppingList: UUID): ShoppingListDto

    suspend fun upsertShoppingList(shoppingListDto: ShoppingListDto)

    suspend fun deleteShoppingListBy(idShoppingList: UUID)
}