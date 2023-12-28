package pl.finitas.app.manage_spendings_feature.presentation.charts.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.data.data_source.CategoryDto
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartType

private val boldness = 300.dp
private val size = 300.dp
private val colors = listOf(
    Color(0XFF2C7B99).copy(.5f),
    Color(0XFF635B7D).copy(.5f),
    Color(0XFFF85784).copy(.5f),
    Color(0xFFFBA776).copy(.5f),
    Color(0xFFFFF06C).copy(.5f),
    Color(0xFFDB6F45).copy(.5f),
    Color(0xFF8AEB7E).copy(.5f),
    Color(0xFF21EE65).copy(.5f),
    Color(0xFF61E4AA).copy(.5f),
    Color(0xFF829FF8).copy(.5f),
    Color(0xFF2E47CF).copy(.5f),
    Color(0xFF6247FA).copy(.5f),
    Color(0xFF42B2DD).copy(.5f),
    Color(0xFFBBADE9).copy(.5f),
    Color(0xFFC24568).copy(.5f),
    Color(0xFFA06B4C).copy(.5f),
    Color(0xFFA79D48).copy(.5f),
    Color(0xFF30552B).copy(.5f),
    Color(0xFF16215C).copy(.5f),
)

@Composable
fun ChartElement(chart: ChartWithCategoriesDto) {
    when (chart.chartType) {
        ChartType.PIE -> PieChartConstructor(chart = chart)
        ChartType.BAR -> BarChartConstructor(chart = chart)
    }
}

@Composable
private fun PieChartConstructor(chart: ChartWithCategoriesDto) {
    Column(
        modifier = Modifier.height(size),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PieChart(
            pieChartData = PieChartData(
                slices = chart.categories.mapIndexed { index, category ->
                    PieChartData.Slice(
                        value = category.spendings.sumOf { it.price }.toFloat(),
                        color = colors[index]
                    )
                }
            ),
            modifier = Modifier
                .width(boldness)
                .height(size - 100.dp),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer(),
        )
        PieChartLabels(
            chart = chart,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PieChartLabels(
    chart: ChartWithCategoriesDto,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .width(300.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        (chart.categories.chunked(3)).forEachIndexed { index, pair ->
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
            tint = colors[index],
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Fonts.regularMini.Text(text = name)
    }
}

@Composable
private fun BarChartConstructor(chart: ChartWithCategoriesDto) {
    BarChart(
        barChartData = BarChartData(
            bars = chart.categories.mapIndexed { index, category ->
                BarChartData.Bar(
                    value = category.spendings.sumOf { it.price }.toFloat(),
                    color = colors[index],
                    label = category.categoryName
                )
            }),
        modifier = Modifier
            .width(boldness)
            .height(size),
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(axisLineColor = Color.White),
        yAxisDrawer = SimpleYAxisDrawer(axisLineColor = Color.White, labelTextColor = Color.White),
        labelDrawer = SimpleValueDrawer(labelTextColor = Color.White)
    )
}

@Composable
private fun LineChartConstructor(chart: ChartWithCategoriesDto) {
    LineChart(
        linesChartData = stubLineChartData.values.map {
            LineChartData(
                it.points.map { point ->
                    LineChartData.Point(
                        value = point.value,
                        label = point.label
                    )
                },
                lineDrawer = SolidLineDrawer(),
            )
        },
        modifier = Modifier
            .width(boldness)
            .height(size),
        animation = simpleChartAnimation(),
        pointDrawer = FilledCircularPointDrawer(),
        horizontalOffset = 5f,
        labels = chart.categories
            .flatMap {
                it.spendings.map { sp -> sp.purchaseDate }
            }
            .toSet()
            .map { date -> "${date.dayOfMonth}.${date.month}" }
    )
}

