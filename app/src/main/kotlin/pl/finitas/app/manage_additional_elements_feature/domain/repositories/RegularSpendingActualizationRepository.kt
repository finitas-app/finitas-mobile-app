package pl.finitas.app.manage_additional_elements_feature.domain.repositories

import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto

interface RegularSpendingActualizationRepository {
    suspend fun upsertFinishedSpendingWithRecords(totalSpending: FinishedSpendingWithRecordsDto)
    suspend fun getRegularSpendings(): List<RegularSpendingWithSpendingDataDto>
}
