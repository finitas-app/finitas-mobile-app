 package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.BigDecimalSerializer
import pl.finitas.app.core.domain.dto.SerializableUUID
import pl.finitas.app.core.domain.dto.UUIDSerializer
import java.util.UUID

@Serializable
data class SynchronizationRequest<T>(
    val lastSyncVersion: Int,
    val idUser: SerializableUUID,
    val objects: List<T>,
)

@Serializable
data class SynchronizationResponse<T>(
    val actualizedSyncVersion: Int,
    val objects: List<T>,
)

@Serializable
data class RemoteSpendingRecordDataDto(
    val idSpendingRecordData: SerializableUUID,
    val name: String,
    @Serializable(BigDecimalSerializer::class)
    val idCategory: SerializableUUID,
)

@Serializable
data class CategoryDto(
    @Serializable(UUIDSerializer::class)
    val idCategory: UUID,
    val name: String,
    @Serializable(UUIDSerializer::class)
    val idParent: UUID?,
)
