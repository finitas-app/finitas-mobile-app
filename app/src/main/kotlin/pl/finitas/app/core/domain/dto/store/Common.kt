package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessage(val message: String)

@Serializable
data class UpdateResponse(val lastSyncVersion: Int)

@Serializable
data class SynchronizationRequest<T>(
    val lastSyncVersion: Int,
    val isAuthorDataToUpdate: Boolean,
    val objects: List<T>
)

@Serializable
data class SynchronizationResponse<T>(
    val actualizedSyncVersion: Int,
    val objects: List<T>
)

@Serializable
data class SpendingRecordDataDto(
    val idSpendingRecordData: String,
    val name: String,
    // todo: serialize as big decimal
    val price: Double,
    val category: CategoryDto,
)

@Serializable
data class CategoryDto(
    val idCategory: String,
    val name: String,
    val idParent: String?,
)
