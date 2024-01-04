package pl.finitas.app.core.domain.repository

import pl.finitas.app.core.data.model.ShoppingListVersion
import pl.finitas.app.core.domain.dto.store.DeleteShoppingListRequest
import pl.finitas.app.core.domain.dto.store.FetchUpdatesResponse
import pl.finitas.app.core.domain.dto.store.RemoteShoppingListDto

interface ShoppingListStoreRepository {
    suspend fun changeShoppingLists(request: List<RemoteShoppingListDto>)
    suspend fun synchronizeShoppingLists(request: List<ShoppingListVersion>): List<FetchUpdatesResponse<RemoteShoppingListDto>>
    suspend fun getAllShoppingLists(idUser: String): List<RemoteShoppingListDto>
    suspend fun createShoppingList(dto: RemoteShoppingListDto)
    suspend fun updateShoppingList(dto: RemoteShoppingListDto)
    suspend fun deleteShoppingList(request: DeleteShoppingListRequest)
}