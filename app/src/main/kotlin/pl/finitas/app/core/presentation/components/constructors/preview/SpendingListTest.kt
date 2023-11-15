package pl.finitas.app.core.presentation.components.constructors.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.constructors.GestureVerticalMenu
import pl.finitas.app.core.presentation.components.constructors.SpendingCategory
import pl.finitas.app.core.presentation.components.constructors.SpendingList
import pl.finitas.app.core.presentation.components.constructors.SpendingRecord
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun SpendingListTest() {
    PrimaryBackground {
        GestureVerticalMenu(
            topLimit = 0.25f,
            bottomLimit = .75f,
        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SpendingList(
                    spendingElements = testData,
                    modifier = Modifier.padding(horizontal = 30.dp),
                    itemExtras = {
                        Row(Modifier.padding(end = 20.dp)) {
                            Fonts.regular.Text(text = it.totalPrice.toString())
                        }
                    }
                )
            }
        }
    }
}

val testData = listOf(
    SpendingCategory(
        "Category 1",
        listOf(),
    ),
    SpendingCategory(
        "Category 2",
        listOf(),
    ),
    SpendingCategory(
        "Category 3",
        listOf(
            SpendingCategory(
                "Category 4",
                listOf(
                    SpendingCategory(
                        "Category 5",
                        listOf(),
                    ),
                    SpendingRecord(
                        "Spending 1",
                        "123.12".toBigDecimal()
                    ),
                    SpendingRecord(
                        "Spending 2",
                        "123.12".toBigDecimal()
                    ),
                    SpendingCategory(
                        "Category 6",
                        listOf(
                            SpendingRecord(
                                "Spending 1",
                                    "123.12".toBigDecimal()
                            ),
                            SpendingRecord(
                                "Spending 2",
                                    "123.12".toBigDecimal()
                            ),
                            SpendingRecord(
                                "Spending 3",
                                    "123.12".toBigDecimal()
                            ),
                        ),
                    ),
                    SpendingCategory(
                        "Category 7",
                        listOf(),
                    ),
                    SpendingCategory(
                        "Category 8",
                        listOf(),
                    ),
                ),
            ),
            SpendingCategory(
                "Category 9",
                listOf(),
            ),
            SpendingCategory(
                "Category 10",
                listOf(),
            ),
            SpendingCategory(
                "Category 11",
                listOf(),
            ),
        ),
    ),
    SpendingCategory(
        "Category 12",
        listOf(),
    ),
)
