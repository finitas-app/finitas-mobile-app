package pl.finitas.app.manage_spendings_feature.domain.model

import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableBigDecimal

@Serializable
data class ParsedEntry(val title: String, val number: SerializableBigDecimal)

@Serializable
data class ReceiptParseResult(val entries: List<ParsedEntry>)

@Serializable
data class Base64Receipt(
    val value: String
)
