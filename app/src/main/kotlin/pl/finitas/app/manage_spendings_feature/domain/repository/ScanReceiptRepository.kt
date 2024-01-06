package pl.finitas.app.manage_spendings_feature.domain.repository

import pl.finitas.app.manage_spendings_feature.domain.model.ReceiptParseResult

interface ScanReceiptRepository {
    suspend fun scanReceipt(file: ByteArray): ReceiptParseResult
}