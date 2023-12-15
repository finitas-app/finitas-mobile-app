package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable

@Serializable
data class GetVisibleNamesRequest(
    val userIds: List<String>
)

@Serializable
data class IdUserWithVisibleName(
    val idUser: String,
    val visibleName: String,
)

@Serializable
data class UserDto(
    val idUser: String,
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>
)

@Serializable
data class UserDataDto(
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>
)

@Serializable
data class RegularSpendingDto(
    val idRegularSpending: String,
    val cron: String,
    val spendingSummary: SpendingSummaryDto,
)