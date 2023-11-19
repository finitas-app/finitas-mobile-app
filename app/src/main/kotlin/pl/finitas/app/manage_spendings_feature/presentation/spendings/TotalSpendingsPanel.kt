package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.GestureVerticalMenu
import pl.finitas.app.core.presentation.components.constructors.SpendingList
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun BoxScope.TotalSpendingsPanel(
    viewModel: TotalSpendingViewModel,
) {
    GestureVerticalMenu(
        topLimit = 0.25f,
        bottomLimit = .75f,
    ) {
        val spendings = viewModel.totalSpendings.collectAsState(listOf())

        Column(Modifier.verticalScroll(rememberScrollState())) {

            spendings.value.forEach { (date, spendings) ->

                Column {
                    Fonts.regular.Text(text = date.toString())
                    SpendingList(
                        spendingElements = spendings,
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
}