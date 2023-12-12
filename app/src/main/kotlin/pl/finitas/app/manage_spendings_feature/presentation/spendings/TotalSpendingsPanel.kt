package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.GestureVerticalMenu
import pl.finitas.app.core.presentation.components.constructors.SpendingList
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.domain.util.convertToView

@Composable
fun BoxScope.TotalSpendingsPanel(
    viewModel: TotalSpendingViewModel,
    onAddSpendingClick: () -> Unit,
) {
    GestureVerticalMenu(
        topLimit = 0f,
        bottomLimit = .55f,
    ) {
        val spendings = viewModel.totalSpendings.collectAsState(listOf())

        TotalSpendingHeader(
            onAddSpendingClick = onAddSpendingClick,
        )

        Column(Modifier.verticalScroll(rememberScrollState()).padding(bottom = 100.dp)) {
            spendings.value.forEach { (date, spendings) ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 12.dp),
                ) {
                    Fonts.regular.Text(
                        text = date.convertToView(),
                        color = Color.White.copy(.6f),
                        modifier = Modifier
                            .padding(start = 10.dp, bottom = 5.dp),
                    )
                    SpendingList(
                        spendingElements = spendings,
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

@Composable
private fun TotalSpendingHeader(
    onAddSpendingClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 28.dp, end = 15.dp, bottom = 5.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Fonts.heading1.Text(
            text = "Spendings",
        )
        ClickableIcon(
            imageVector = Icons.Rounded.AddCircle,
            onClick = onAddSpendingClick,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}