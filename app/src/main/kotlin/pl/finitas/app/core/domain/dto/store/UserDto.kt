package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.UUIDSerializer
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
    val visibleName: String,
)

@Serializable
data class VisibleName(
    val value: String
)

@Serializable
data class IdRegularSpending(
    @Serializable(UUIDSerializer::class)
    val id: UUID
)

@Serializable
data class UserDto(
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>
)

@Serializable
data class RegularSpendingDto(
    @Serializable(UUIDSerializer::class)
    val idRegularSpending: UUID,
    val cron: String,
    val spendingSummary: SpendingSummaryDto,
)