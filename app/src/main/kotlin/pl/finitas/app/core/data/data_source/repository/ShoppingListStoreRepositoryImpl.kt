package pl.finitas.app.core.data.data_source.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pl.finitas.app.core.domain.dto.store.DeleteShoppingListRequest
import pl.finitas.app.core.domain.dto.store.RemoteShoppingListDto
import pl.finitas.app.core.domain.dto.store.SynchronizationRequest
import pl.finitas.app.core.domain.dto.store.SynchronizationResponse
import pl.finitas.app.core.domain.repository.ShoppingListStoreRepository
import pl.finitas.app.core.http.HttpUrls

class ShoppingListStoreRepositoryImpl(private val httpClient: HttpClient) :
    ShoppingListStoreRepository {

    override suspend fun synchronizeShoppingLists(request: SynchronizationRequest<RemoteShoppingListDto>): SynchronizationResponse<RemoteShoppingListDto> {
        return httpClient.put("${HttpUrls.shoppingListsStore}/synchronize") {
            setBody(request)
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getAllShoppingLists(idUser: String): List<RemoteShoppingListDto> {
        return httpClient.get("${HttpUrls.shoppingListsStore}/$idUser") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun createShoppingList(dto: RemoteShoppingListDto) {
        httpClient.post(HttpUrls.shoppingListsStore) {
            setBody(dto)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun updateShoppingList(dto: RemoteShoppingListDto) {
        httpClient.patch(HttpUrls.shoppingListsStore) {
            setBody(dto)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteShoppingList(request: DeleteShoppingListRequest) {
        httpClient.delete(HttpUrls.shoppingListsStore) {
            setBody(request)
            contentType(ContentType.Application.Json)
        }
    }
}