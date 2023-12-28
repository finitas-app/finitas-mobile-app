package pl.finitas.app.manage_spendings_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Chart
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartDtoWithCategoryIds
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto

interface ChartRepository {
    fun getChartsWithCategoriesFlow(): Flow<List<ChartWithCategoriesDto>>
    suspend fun upsertChart(chart: ChartDtoWithCategoryIds)
    suspend fun getAllCharts(): List<Chart>
    suspend fun deleteChartWithCategoryRefs(chart: ChartWithCategoriesDto)
}