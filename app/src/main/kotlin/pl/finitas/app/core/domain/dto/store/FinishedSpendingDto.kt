package pl.finitas.app.core.domain.dto.store

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableBigDecimal
import pl.finitas.app.core.domain.dto.SerializableLocalDateTime
import pl.finitas.app.core.domain.dto.SerializableUUID

@Serializable
data class DeleteFinishedSpendingRequest(
    val idSpendingSummary: SerializableUUID,
    val idUser: SerializableUUID,
)

@Serializable
class FinishedSpendingDto(
    val idSpendingSummary: SerializableUUID,
    val idReceipt: SerializableUUID?,
    val purchaseDate: SerializableLocalDateTime,
    override val version: Int,
    override val idUser: SerializableUUID,
    override val isDeleted: Boolean,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
): SynchronizableEntity

@Serializable
data class SpendingRecordDto(
    val idSpendingRecordData: SerializableUUID,
    val name: String,
    val price: SerializableBigDecimal,
    val idCategory: SerializableUUID,
)