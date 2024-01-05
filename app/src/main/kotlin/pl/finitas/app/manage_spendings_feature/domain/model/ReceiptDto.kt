package pl.finitas.app.manage_spendings_feature.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptParseResult(val result: Map<String, String>)