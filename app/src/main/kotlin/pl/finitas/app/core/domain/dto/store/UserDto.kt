package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.LocalDateTimeSerializer
import pl.finitas.app.core.domain.dto.UUIDSerializer
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class UserIdValue(
    @Serializable(UUIDSerializer::class)
    val userId: UUID
)

@Serializable
data class GetVisibleNamesRequest(
    val userIds: List<UserIdValue>
)

@Serializable
data class IdUserWithVisibleName(
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val visibleName: String?,
)

@Serializable
data class VisibleName(
    val value: String
)

@Serializable
data class IdSpendingSummary(
    @Serializable(UUIDSerializer::class)
    val idSpendingSummary: UUID
)

@Serializable
data class UserDto(
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>,
)

@Serializable
data class RegularSpendingDto(
    val actualizationPeriod: Int,
    val periodUnit: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastActualizationDate: LocalDateTime,
    @Serializable(UUIDSerializer::class)
    val idSpendingSummary: UUID,
    @Serializable(LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val name: String,
    val spendingRecords: List<RemoteSpendingRecordDto>,
)