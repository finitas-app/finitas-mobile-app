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
import pl.finitas.app.core.presentation.components.constructors.SpendingList
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
                    spendingElements = listOf(),
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