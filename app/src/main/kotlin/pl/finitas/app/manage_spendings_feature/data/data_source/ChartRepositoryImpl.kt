package pl.finitas.app.manage_spendings_feature.data.data_source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.data_source.dao.ChartDao
import pl.finitas.app.core.data.data_source.dao.ChartWithCategoryFlat
import pl.finitas.app.core.data.model.Chart
import pl.finitas.app.core.data.model.ChartToCategoryRef
import pl.finitas.app.core.data.model.relations.ChartToCategoryRefs
import pl.finitas.app.core.domain.validateBuilder
import pl.finitas.app.manage_spendings_feature.domain.repository.ChartRepository
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ChartRepositoryImpl(val dao: ChartDao) : ChartRepository {
    override fun getChartsWithCategoriesFlow(
        idRoom: UUID?,
        idTargetUser: UUID?,
    ): Flow<List<ChartWithCategoriesDto>> {
        require((idRoom != null && idTargetUser != null).not())
        return (if (idRoom != null) {
            dao.getChartsWithCategoriesFlatByIdRoomFlow(idRoom)
        } else if (idTargetUser != null) {
            dao.getChartsWithCategoriesFlatByIdTargetUserFlow(idTargetUser)
        } else {
            dao.getChartsWithCategoriesFlatFlow()
        }).map { it.toDto() }
    }

    override suspend fun upsertChart(chart: ChartDtoWithCategoryIds) {
        dao.upsertChartWithCategories(
            ChartToCategoryRefs(
                chart = Chart(
                    idChart = chart.idChart,
                    startDate = chart.startDate?.atStartOfDay(),
                    endDate = chart.endDate?.atStartOfDay(),
                    chartType = chart.chartType.ordinal,
                    idTargetUser = chart.idTargetUser,
                    idRoom = chart.idRoom,
                ),
                categoryRefs = chart.categoryIds.map {
                    ChartToCategoryRef(
                        idChart = chart.idChart,
                        idCategory = it,
                    )
                }
            )
        )
    }

    override suspend fun deleteChartWithCategoryRefs(chart: ChartWithCategoriesDto) {
        dao.deleteChart(
            Chart(
                idChart = chart.idChart,
                startDate = chart.startDate?.atStartOfDay(),
                endDate = chart.endDate?.atStartOfDay(),
                chartType = chart.chartType.ordinal,
                idTargetUser = chart.idTargetUser,
                idRoom = chart.idRoom,
            )
        )
    }
}

private fun List<ChartWithCategoryFlat>.toDto() = groupBy { it.idChart }
    .values
    .map {
        val firstFlattened = it.first()
        ChartWithCategoriesDto(
            idChart = firstFlattened.idChart,
            chartType = ChartType.entries[firstFlattened.chartType],
            startDate = firstFlattened.startDate?.toLocalDate(),
            endDate = firstFlattened.endDate?.toLocalDate(),
            idTargetUser = firstFlattened.idTargetUser,
            idRoom = firstFlattened.idRoom,
            categories = mapToCategoryDtoList(it)
        )
    }

private fun mapToCategoryDtoList(flatValues: List<ChartWithCategoryFlat>) =
    flatValues.groupBy { it.idCategory }
        .values
        .map {
            val first = it.first()
            ChartCategoryDto(
                categoryName = first.categoryName,
                idCategory = first.idCategory,
                spendings = it.map { flat ->
                    SpendingPoint(
                        purchaseDate = flat.purchaseDate,
                        price = flat.price
                    )
                }
            )
        }

data class ChartDtoWithCategoryIds(
    val idChart: UUID,
    val chartType: ChartType,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val idTargetUser: UUID?,
    val idRoom: UUID?,
    val categoryIds: Set<UUID>,
) {
    companion object {
        const val BAR_CHART_MAX_CATEGORIES_SIZE = 10
        const val PIE_CHART_MAX_CATEGORIES_SIZE = 20
    }

    init {
        validateBuilder {
            validate(categoryIds.isNotEmpty()) { "Chart should have at least one category" }
            validate(chartType == ChartType.BAR || categoryIds.size <= BAR_CHART_MAX_CATEGORIES_SIZE) {
                "Bar chart can include maximum $BAR_CHART_MAX_CATEGORIES_SIZE categories"
            }
            validate(chartType == ChartType.PIE || categoryIds.size <= PIE_CHART_MAX_CATEGORIES_SIZE) {
                "Pie chart can include maximum $PIE_CHART_MAX_CATEGORIES_SIZE categories"
            }
            validate(startDate == null || endDate == null || startDate < endDate) {
                "Start date can not be after end date"
            }
        }
    }
}

data class ChartWithCategoriesDto(
    val idChart: UUID,
    val chartType: ChartType,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val categories: List<ChartCategoryDto>,
    val idTargetUser: UUID?,
    val idRoom: UUID?,
)

data class ChartCategoryDto(
    val categoryName: String,
    val idCategory: UUID,
    val spendings: List<SpendingPoint>,
)

data class SpendingPoint(
    val purchaseDate: LocalDateTime,
    val price: BigDecimal,
)