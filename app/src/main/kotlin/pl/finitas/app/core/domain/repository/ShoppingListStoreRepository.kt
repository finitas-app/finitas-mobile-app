package pl.finitas.app.core.domain.repository

import pl.finitas.app.core.domain.dto.store.DeleteShoppingListRequest
import pl.finitas.app.core.domain.dto.store.RemoteShoppingListDto
import pl.finitas.app.core.domain.dto.store.SynchronizationRequest
import pl.finitas.app.core.domain.dto.store.SynchronizationResponse

interface ShoppingListStoreRepository {
    suspend fun synchronizeShoppingLists(request: SynchronizationRequest<RemoteShoppingListDto>): SynchronizationResponse<RemoteShoppingListDto>
    suspend fun getAllShoppingLists(idUser: String): List<RemoteShoppingListDto>
    suspend fun createShoppingList(dto: RemoteShoppingListDto)
    suspend fun updateShoppingList(dto: RemoteShoppingListDto)
    suspend fun deleteShoppingList(request: DeleteShoppingListRequest)
}