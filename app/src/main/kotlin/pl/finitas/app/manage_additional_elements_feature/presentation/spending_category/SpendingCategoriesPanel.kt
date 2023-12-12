package pl.finitas.app.manage_additional_elements_feature.presentation.spending_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.LayeredList
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun SpendingCategoryPanel(
    viewModel: SpendingCategoryViewModel,
    modifier: Modifier = Modifier,
) {
    val categories by viewModel.categories.collectAsState(initial = listOf())
    Column(modifier = modifier) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ){
            Fonts.heading1.Text(text = "My categories")
            ClickableIcon(
                imageVector = Icons.Rounded.AddCircle,
                onClick = {
                    viewModel.openUpsertCategoryDialog(
                        SpendingCategoryEvent.AddSpendingCategoryEvent(null)
                    )
                },
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        if (categories.isNotEmpty()) {
            ConstructorBox (Modifier.padding(top = 12.dp)){
                LayeredList<SpendingElementView>(
                    nameableCollections = categories,
                    itemExtras = {
                        ClickableIcon(
                            imageVector = Icons.Rounded.AddCircle,
                            onClick = {
                                viewModel.openUpsertCategoryDialog(
                                    SpendingCategoryEvent.AddSpendingCategoryEvent((it as SpendingCategoryView).idCategory)
                                )
                            },
                            modifier = Modifier.padding(end = 20.dp),
                        )
                    },
                    modifier = Modifier.padding(24.dp)
                )
            }
        }

    }
}