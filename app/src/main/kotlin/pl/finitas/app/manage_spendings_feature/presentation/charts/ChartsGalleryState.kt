package pl.finitas.app.manage_spendings_feature.presentation.charts

import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import java.time.LocalDate
import java.util.UUID

data class ChartData(
    val value: Float,
    val label: String,
)

data class SimpleChartState(
    val values: List<ChartData>
) {
    companion object {
        fun from(dto: ChartWithCategoriesDto) {
            SimpleChartState(
                values = dto.categories.map {
                    ChartData(
                        label = it.categoryName,
                        value = it.spendings.sumOf { spending -> spending.price }.toFloat()
                    )
                }
            )
        }
    }
}

data class SingleLineState(
    val label: String,
    val points: List<ChartData>
)

data class LineChartState(
    val values: List<SingleLineState>
)

enum class ChartType {
    PIE,
    BAR,
}

data class ChartState(
    val chartId: UUID,
    val chartType: ChartType,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val categories: List<SpendingCategoryView>
) {
    companion object {
        fun empty() = ChartState(
            chartType = ChartType.PIE,
            startDate = null,
            endDate = null,
            categories = listOf(),
            chartId = UUID.randomUUID()
        )
    }
}
