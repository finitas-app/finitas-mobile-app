package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.GestureVerticalMenu
import pl.finitas.app.core.presentation.components.constructors.LayeredList
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.domain.util.convertToView
import java.util.UUID

@Composable
fun BoxScope.FinishedSpendingsPanel(
    viewModel: FinishedSpendingViewModel,
    onAddSpendingClick: (FinishedSpendingView?, idUser: UUID?) -> Unit,
    hasModifyAuthority: Boolean,
    context: FinishedSpendingContext,
) {
    GestureVerticalMenu(
        topLimit = 0f,
        bottomLimit = .55f,
    ) {
        val spendings by viewModel.totalSpendings.collectAsState(listOf())

        TotalSpendingHeader(
            onAddSpendingClick = {
                when(context.idsUser.size) {
                    1 -> onAddSpendingClick(null, context.idsUser[0])
                    else -> onAddSpendingClick(null, null)
                }
            },
            hasModifyAuthority = hasModifyAuthority,
        )

        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            spendings.forEach { (date, spendings) ->
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
                    LayeredList<SpendingElementView>(
                        nameableCollection = spendings,
                        itemExtras = {
                            Row(Modifier.padding(end = 20.dp)) {
                                Fonts.regular.Text(text = it.totalPrice.toString())
                            }
                        },
                        onNameClick = { spendingElement ->
                            if (hasModifyAuthority && spendingElement is FinishedSpendingView) {
                                onAddSpendingClick(spendingElement, spendingElement.idUser)
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
    hasModifyAuthority: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 28.dp, end = 15.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Fonts.heading1.Text(
            text = "Spendings",
            Modifier.padding(vertical = 10.dp)
        )
        if (hasModifyAuthority) {
            ClickableIcon(
                imageVector = Icons.Rounded.AddCircle,
                onClick = onAddSpendingClick,
                modifier = Modifier.padding(top = 5.dp)
            )
        } else {
            Box(Modifier)
        }
    }
}