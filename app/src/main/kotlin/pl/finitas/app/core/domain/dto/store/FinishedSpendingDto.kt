package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.LocalDateTimeSerializer
import pl.finitas.app.core.domain.dto.UUIDSerializer
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class DeleteFinishedSpendingRequest(
    @Serializable(UUIDSerializer::class)
    val idSpendingSummary: UUID,
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
)

@Serializable
class FinishedSpendingDto(
    val spendingSummary: SpendingSummaryDto,
    @Serializable(UUIDSerializer::class)
    val idReceipt: UUID?,
    @Serializable(LocalDateTimeSerializer::class)
    val purchaseDate: LocalDateTime,
    override val version: Int,
    @Serializable(UUIDSerializer::class)
    override val idUser: UUID,
    override val isDeleted: Boolean,
): SynchronizableEntity

@Serializable
data class SpendingSummaryDto(
    @Serializable(UUIDSerializer::class)
    val idSpendingSummary: UUID,
    @Serializable(LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
)

@Serializable
data class SpendingRecordDto(
    @Serializable(UUIDSerializer::class)
    val idSpendingRecord: UUID,
    val spendingRecordData: RemoteSpendingRecordDataDto,
)