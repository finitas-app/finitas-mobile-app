package pl.finitas.app.manage_spendings_feature.domain.service

import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_spendings_feature.domain.repository.ScanReceiptRepository
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.util.UUID

class ScanReceiptService(val repository: ScanReceiptRepository) {
    suspend fun scanReceipt(file: ByteArray, idCategory: UUID): List<SpendingRecordView> {
        return repository.scanReceipt(file)
            .entries
            .map {
                SpendingRecordView(
                    name = it.title,
                    totalPrice = it.number,
                    idCategory = idCategory,
                    idSpendingRecord = UUID.randomUUID(),
                    currency = CurrencyValue.PLN, // TODO: to remove
                )
            }
    }
}