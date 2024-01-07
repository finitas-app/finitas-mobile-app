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
    fun getChartsWithCategoriesFlow(idRoom: UUID?, idTargetUser: UUID?) = chartRepository.getChartsWithCategoriesFlow(idRoom, idTargetUser)

    suspend fun getSpendingCategories(idRoom: UUID?, idTargetUser: UUID?): List<SpendingCategoryView> {
        val categories = when {
            idRoom != null -> spendingCategoryRepository.getAllUsersSpendingCategories()
            idTargetUser != null -> spendingCategoryRepository.getSpendingCategoriesByIdUser(idTargetUser)
            else -> spendingCategoryRepository.getSpendingCategories()
        }
        val (originCategories, nestedCategories) = categories
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
                idTargetUser = chartState.idTargetUser,
                idRoom = chartState.idRoom,
                currencyValue = chartState.currencyValue,
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