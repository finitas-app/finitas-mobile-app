package pl.finitas.app.manage_spendings_feature.domain.service

import pl.finitas.app.manage_spendings_feature.domain.repository.ScanReceiptRepository

class ScanReceiptService(val repository: ScanReceiptRepository) {
    suspend fun scanReceipt(file: ByteArray) = repository.scanReceipt(file)
}