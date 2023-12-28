package pl.finitas.app.manage_spendings_feature.domain.service

import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartDtoWithCategoryIds
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.domain.repository.ChartRepository
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartState
import java.util.UUID

class ChartService(
    private val chartRepository: ChartRepository,
    private val spendingCategoryRepository: SpendingCategoryRepository,
) {
    fun getChartsWithCategoriesFlow() = chartRepository.getChartsWithCategoriesFlow()

    suspend fun getSpendingCategories(): List<SpendingCategoryView> {
        val (originCategories, nestedCategories) = spendingCategoryRepository.getSpendingCategories()
            .partition { it.idParent == null }
        val categoriesByParentId = nestedCategories.groupBy { it.idParent!! }.toMutableMap()

        return getSpendingCategoriesTreeRecursive(
            originCategories,
            categoriesByParentId
        )
    }

    suspend fun upsertChart(chartState: ChartState, categoryIds: Set<UUID>) {
        chartRepository.upsertChart(
            chart = ChartDtoWithCategoryIds(
                idChart = chartState.chartId,
                chartType = chartState.chartType,
                startDate = chartState.startDate,
                endDate = chartState.endDate,
                categoryIds = categoryIds,
            ),
        )
    }

    suspend fun deleteChartWithCategoryRefs(chart: ChartWithCategoriesDto) {
        chartRepository.deleteChartWithCategoryRefs(chart = chart)
    }
}

private fun getSpendingCategoriesTreeRecursive(
    currentCategories: List<SpendingCategory>,
    categoriesByParentId: Map<UUID, List<SpendingCategory>>,
): List<SpendingCategoryView> = currentCategories.sortedBy { it.name }.map {
    SpendingCategoryView(
        name = it.name,
        idCategory = it.idCategory,
        elements = getSpendingCategoriesTreeRecursive(
            categoriesByParentId[it.idCategory] ?: listOf(),
            categoriesByParentId,
        )
    )
}