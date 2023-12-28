package pl.finitas.app.manage_spendings_feature.presentation.charts.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.data.data_source.CategoryDto
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartType

@Composable
fun ChartElement(
    chart: ChartWithCategoriesDto,
    modifier: Modifier,
) {
    when (chart.chartType) {
        ChartType.PIE -> PieChartConstructor(
            chart = chart,
            modifier = modifier,
        )
        ChartType.BAR -> BarChartConstructor(
            chart = chart,
            modifier = modifier,
        )
    }
}

@Composable
private fun PieChartConstructor(
    chart: ChartWithCategoriesDto,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(
            pieChartData = PieChartData(
                slices = chart.categories.mapIndexed { index, category ->
                    PieChartData.Slice(
                        value = category.spendings.sumOf { it.price }.toFloat(),
                        color = CHART_COLORS[index]
                    )
                }
            ),
            modifier = Modifier
                .weight(.9f),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer(),
        )
        PieChartLabels(
            chart = chart,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun PieChartLabels(
    chart: ChartWithCategoriesDto,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
    ) {
        chart.categories.chunked(2).forEachIndexed { index, pair ->
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                pair.forEachIndexed { innerIndex, it ->
                    PieChartLabel(category = it, index = index * 3 + innerIndex)
                }
            }
        }
    }
}

@Composable
private fun PieChartLabel(category: CategoryDto, index: Int) {
    val name = if (category.categoryName.length > 8)
        "${category.categoryName.substring(0, 8)}..."
    else category.categoryName
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Circle,
            contentDescription = "",
            tint = CHART_COLORS[index],
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Fonts.regularMini.Text(text = name)
    }
}

@Composable
private fun BarChartConstructor(
    chart: ChartWithCategoriesDto,
    modifier: Modifier = Modifier,
) {
    BarChart(
        barChartData = BarChartData(
            bars = chart.categories.mapIndexed { index, category ->
                BarChartData.Bar(
                    value = category.spendings.sumOf { it.price }.toFloat(),
                    color = CHART_COLORS[index],
                    label = category.categoryName
                )
            }),
        modifier = modifier,
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(axisLineColor = Color.White),
        yAxisDrawer = SimpleYAxisDrawer(axisLineColor = Color.White, labelTextColor = Color.White),
        labelDrawer = SimpleValueDrawer(labelTextColor = Color.White)
    )
}
