package pl.finitas.app.manage_spendings_feature.presentation.charts

import pl.finitas.app.core.domain.services.SpendingCategoryView
import java.time.LocalDate
import java.util.UUID

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
