package pl.finitas.app.sync_feature.domain.repository

import pl.finitas.app.core.data.model.ShoppingListVersion
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListDto
import java.util.UUID

interface ShoppingListSyncRepository {
    suspend fun getNotSynchronizedShoppingLists(): List<ShoppingListDto>

    suspend fun setShoppingListVersion(actualShoppingListVersion: ShoppingListVersion)

    suspend fun upsertShoppingList(shoppingListDto: ShoppingListDto)

    suspend fun deleteMarkedShoppingListAndSynchronized()
    suspend fun deleteByIdUser(idUser: UUID)
}