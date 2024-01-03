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
import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.IdSpendingSummary
import pl.finitas.app.core.domain.dto.store.IdUserWithVisibleName
import pl.finitas.app.core.domain.dto.store.RegularSpendingDto
import pl.finitas.app.core.domain.dto.store.VisibleName
import pl.finitas.app.core.domain.repository.CategoryDto
import pl.finitas.app.core.domain.repository.CategoryVersionDto
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
        regularSpendings: List<RegularSpendingDto>,
    ) {
        httpClient.post("${HttpUrls.usersStore}/regular-spendings") {
            setBody(regularSpendings)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun deleteRegularSpending(idSpendingSummary: IdSpendingSummary) {
        httpClient.delete("${HttpUrls.usersStore}/regular-spendings") {
            setBody(idSpendingSummary)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun syncCategories(userVersions: List<CategoryVersionDto>): SyncCategoriesResponse {
        return httpClient.post("${HttpUrls.usersStore}/categories/sync") {
            setBody(SyncCategoriesRequest(userVersions))
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun changeCategories(changedCategories: List<SpendingCategory>) {
        httpClient.post("${HttpUrls.usersStore}/categories") {
            setBody(
                ChangeSpendingCategoriesRequest(
                    changedCategories.map {
                        ChangeSpendingCategoryDto(
                            name = it.name,
                            idParent = it.idParent,
                            idUser = it.idUser,
                            idCategory = it.idCategory,
                            isDeleted = it.isDeleted,
                        )
                    }
                )
            )
        }
    }
}

@Serializable
data class SyncCategoriesRequest(
    val userVersions: List<CategoryVersionDto>,
)

@Serializable
data class SyncCategoriesResponse(
    val userCategories: List<UserWithCategoriesDto>,
    val unavailableUsers: List<SerializableUUID>,
)

@Serializable
data class UserWithCategoriesDto(
    val idUser: SerializableUUID,
    val categoryVersion: Int,
    val categories: List<CategoryDto>,
)

@Serializable
data class ChangeSpendingCategoriesRequest(
    val spendingCategories: List<ChangeSpendingCategoryDto>,
)



@Serializable
data class ChangeSpendingCategoryDto(
    val name: String,
    val idParent: SerializableUUID?,
    val idUser: SerializableUUID?,
    val idCategory: SerializableUUID,
    val isDeleted: Boolean,
)
