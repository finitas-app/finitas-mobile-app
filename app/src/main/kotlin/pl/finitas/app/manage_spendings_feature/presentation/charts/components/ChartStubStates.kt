package pl.finitas.app.manage_spendings_feature.presentation.charts.components

import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartData
import pl.finitas.app.manage_spendings_feature.presentation.charts.LineChartState
import pl.finitas.app.manage_spendings_feature.presentation.charts.SimpleChartState
import pl.finitas.app.manage_spendings_feature.presentation.charts.SingleLineState

val stubSimpleChartState = SimpleChartState(
    listOf(
        ChartData(value = .1f, label = "Products"),
        ChartData(value = .2f, label = "Drinks"),
        ChartData(value = .3f, label = "Clothes"),
    )
)

val stubLineChartData = LineChartState(
    listOf(
        SingleLineState(
            label = "Products",
            points = listOf(
                ChartData(value = .1f, label = "1.10"),
                ChartData(value = .2f, label = "2.10"),
                ChartData(value = .3f, label = "3.10"),
                ChartData(value = .4f, label = "4.10"),
            )
        ),
        SingleLineState(
            label = "Clothes",
            points = listOf(
                ChartData(value = .5f, label = "1.10"),
                ChartData(value = .4f, label = "2.10"),
                ChartData(value = .7f, label = "3.10"),
                ChartData(value = .1f, label = "4.10"),
            )
        )
    )
)