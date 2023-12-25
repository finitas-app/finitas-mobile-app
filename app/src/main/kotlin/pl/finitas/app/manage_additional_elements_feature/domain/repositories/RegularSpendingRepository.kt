package pl.finitas.app.manage_additional_elements_feature.domain.repositories

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto

interface RegularSpendingRepository {
    fun getRegularSpendingsFlow(): Flow<List<RegularSpendingWithSpendingDataDto>>
    suspend fun upsertFinishedSpendingWithRecords(totalSpending: FinishedSpendingWithRecordsDto)
    suspend fun getRegularSpendings(): List<RegularSpendingWithSpendingDataDto>
    suspend fun upsertRegularSpendingWithRecords(regularSpendingDto: RegularSpendingWithSpendingDataDto)
    suspend fun deleteRegularSpendingWithRecords(regularSpendingDto: RegularSpendingWithSpendingDataDto)
}
