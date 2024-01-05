package pl.finitas.app.manage_spendings_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartDtoWithCategoryIds
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import java.util.UUID

interface ChartRepository {
    fun getChartsWithCategoriesFlow(idRoom: UUID?, idTargetUser: UUID?): Flow<List<ChartWithCategoriesDto>>
    suspend fun upsertChart(chart: ChartDtoWithCategoryIds)
    suspend fun deleteChartWithCategoryRefs(chart: ChartWithCategoriesDto)
}