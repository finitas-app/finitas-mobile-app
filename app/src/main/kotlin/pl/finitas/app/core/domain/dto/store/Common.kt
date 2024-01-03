 package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID

@Serializable
data class SynchronizationRequest<T : SynchronizableEntity>(
    val lastSyncVersion: Int,
    val idUser: SerializableUUID,
    val objects: List<T>,
)

@Serializable
data class SynchronizationResponse<T : SynchronizableEntity>(
    val actualizedSyncVersion: Int,
    val objects: List<T>,
)

 interface SynchronizableEntity {
     val version: Int
     val idUser: SerializableUUID
     val isDeleted: Boolean
 }

