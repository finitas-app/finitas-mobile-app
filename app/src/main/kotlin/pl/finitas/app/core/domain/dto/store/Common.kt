package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.BigDecimalSerializer
import pl.finitas.app.core.domain.dto.UUIDSerializer
import java.math.BigDecimal
import java.util.UUID

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
    @Serializable(UUIDSerializer::class)
    val idSpendingRecordData: UUID,
    val name: String,
    @Serializable(BigDecimalSerializer::class)
    val price: BigDecimal,
    val category: CategoryDto,
)

@Serializable
data class CategoryDto(
    @Serializable(UUIDSerializer::class)
    val idCategory: UUID,
    val name: String,
    @Serializable(UUIDSerializer::class)
    val idParent: UUID?,
)
