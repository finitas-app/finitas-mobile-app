package pl.finitas.app.manage_additional_elements_feature.presentation.spending_category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.LayeredList
import pl.finitas.app.manage_additional_elements_feature.presentation.components.AdditionalElemHeader

@Composable
fun SpendingCategoryPanel(
    viewModel: SpendingCategoryViewModel,
    modifier: Modifier = Modifier,
) {
    val categories by viewModel.categories.collectAsState(initial = listOf())
    Column(modifier = modifier) {
        AdditionalElemHeader(
            title = "My categories",
            addIconOnClick = {
                viewModel.openUpsertCategoryDialog(
                    SpendingCategoryEvent.AddSpendingCategoryEvent(null)
                )
            }
        )

        if (categories.isNotEmpty()) {
            ConstructorBox(Modifier.padding(top = 12.dp)) {
                LayeredList<SpendingElementView>(
                    nameableCollection = categories,
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
                    onNameClick = { spendingElementView ->  
                        if (spendingElementView is SpendingCategoryView) {
                            viewModel.openUpsertCategoryDialog(
                                SpendingCategoryEvent.UpdateSpendingCategoryEvent(spendingElementView.idCategory)
                            )
                        }
                    },
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}