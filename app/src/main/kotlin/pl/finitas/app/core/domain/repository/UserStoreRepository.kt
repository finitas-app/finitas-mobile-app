package pl.finitas.app.core.domain.repository

import kotlinx.serialization.Serializable
import pl.finitas.app.core.data.data_source.repository.SyncCategoriesResponse
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.core.domain.dto.UUIDSerializer
import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.IdSpendingSummary
import pl.finitas.app.core.domain.dto.store.IdUserWithVisibleName
import pl.finitas.app.core.domain.dto.store.RegularSpendingDto
import pl.finitas.app.core.domain.dto.store.VisibleName
import java.util.UUID

interface UserStoreRepository {
    suspend fun addRegularSpendings(idUser: String, regularSpendings: List<RegularSpendingDto>)
    suspend fun getVisibleNames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName>
    suspend fun patchVisibleName(visibleName: VisibleName)
    suspend fun getRegularSpendings(idUser: String): List<RegularSpendingDto>
    suspend fun deleteRegularSpending(idSpendingSummary: IdSpendingSummary)

    suspend fun syncCategories(userVersions: List<CategoryVersionDto>): SyncCategoriesResponse
    suspend fun changeCategories(changedCategories: List<SpendingCategory>)
}

@Serializable
data class CategoryVersionDto(
    val idUser: SerializableUUID,
    val version: Int,
)

@Serializable
data class CategoryDto(
    @Serializable(UUIDSerializer::class)
    val idCategory: UUID,
    val name: String,
    @Serializable(UUIDSerializer::class)
    val idParent: UUID?,
    val version: Int?,
    val isDeleted: Boolean,
)
