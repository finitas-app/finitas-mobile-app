package pl.finitas.app.core.domain.repository

import pl.finitas.app.manage_additional_elements_feature.domain.FinishedSpendingWithRecordsDto
import pl.finitas.app.manage_additional_elements_feature.domain.RegularSpendingWithSpendingDataDto

interface RegularSpendingActualizationRepository {
    suspend fun upsertFinishedSpendingAndRegularSpending(
        regularSpending: RegularSpendingWithSpendingDataDto,
        finishedSpending: FinishedSpendingWithRecordsDto
    )
    suspend fun getRegularSpendings(): List<RegularSpendingWithSpendingDataDto>
}
