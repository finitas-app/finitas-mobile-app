package pl.finitas.app.core.domain.repository

import pl.finitas.app.core.domain.dto.store.DeleteShoppingListRequest
import pl.finitas.app.core.domain.dto.store.ShoppingListDto
import pl.finitas.app.core.domain.dto.store.SynchronizationRequest
import pl.finitas.app.core.domain.dto.store.SynchronizationResponse

interface ShoppingListStoreRepository {
    suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>): SynchronizationResponse<ShoppingListDto>
    suspend fun getAllShoppingLists(idUser: String): List<ShoppingListDto>
    suspend fun createShoppingList(dto: ShoppingListDto)
    suspend fun updateShoppingList(dto: ShoppingListDto)
    suspend fun deleteShoppingList(request: DeleteShoppingListRequest)
}