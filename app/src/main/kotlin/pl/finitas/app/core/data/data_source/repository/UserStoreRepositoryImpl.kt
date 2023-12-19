package pl.finitas.app.core.data.data_source.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.IdRegularSpending
import pl.finitas.app.core.domain.dto.store.IdUserWithVisibleName
import pl.finitas.app.core.domain.dto.store.RegularSpendingDto
import pl.finitas.app.core.domain.dto.store.VisibleName
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.core.http.HttpUrls

class UserStoreRepositoryImpl(private val httpClient: HttpClient) : UserStoreRepository {

    override suspend fun patchVisibleName(visibleName: VisibleName) {
        httpClient.patch("${HttpUrls.usersStore}/nicknames") {
            setBody(visibleName)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getVisibleNames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName> {
        return httpClient.post("${HttpUrls.usersStore}/nicknames/sync") {
            setBody(request)
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getRegularSpendings(idUser: String): List<RegularSpendingDto> {
        return httpClient.get("${HttpUrls.usersStore}/regular-spendings"){
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun addRegularSpendings(
        idUser: String,
        regularSpendings: List<RegularSpendingDto>
    ) {
        httpClient.post("${HttpUrls.usersStore}/regular-spendings") {
            setBody(regularSpendings)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteRegularSpending(idRegularSpending: IdRegularSpending) {
        httpClient.delete("${HttpUrls.usersStore}/regular-spendings") {
            setBody(idRegularSpending)
            contentType(ContentType.Application.Json)
        }
    }
}