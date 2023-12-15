package pl.finitas.app.core.data.data_source.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.IdUserWithVisibleName
import pl.finitas.app.core.domain.dto.store.RegularSpendingDto
import pl.finitas.app.core.domain.dto.store.UserDto
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.core.http.HttpUrls

class UserStoreRepositoryImpl(private val httpClient: HttpClient) : UserStoreRepository {

    override suspend fun addRegularSpendings(
        idUser: String,
        regularSpendings: List<RegularSpendingDto>
    ) {
        httpClient.post("${HttpUrls.usersStore}/$idUser/regular-spendings") {
            setBody(regularSpendings)
        }
    }

    override suspend fun upsertUser(dto: UserDto) {
        return httpClient.put(HttpUrls.usersStore) {
            setBody(dto)
        }.body()
    }

    override suspend fun getNicknames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName> {
        return httpClient.get("${HttpUrls.usersStore}/nicknames") {
            setBody(request)
        }.body()
    }

    override suspend fun getRegularSpendings(idUser: String): List<RegularSpendingDto> {
        return httpClient.get("${HttpUrls.usersStore}/$idUser/regular-spendings").body()
    }
}