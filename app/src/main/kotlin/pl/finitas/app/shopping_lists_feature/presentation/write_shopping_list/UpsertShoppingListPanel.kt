package pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.dialog.CustomDialog
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list.components.CategoryShoppingList

@Composable
fun UpsertShoppingList(
    upsertShoppingListViewModel: UpsertShoppingListViewModel,
) {
    CustomDialog(
        isOpened = upsertShoppingListViewModel.isDialogOpen,
        onDismissRequest = upsertShoppingListViewModel::closeDialog,
        onConfirmRequest = upsertShoppingListViewModel::onSave,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        AddSpendingPanel(upsertShoppingListViewModel)
    }
}

@Composable
private fun AddSpendingPanel(
    upsertShoppingListViewModel: UpsertShoppingListViewModel,
) {

    AddSpendingForm(upsertShoppingListViewModel = upsertShoppingListViewModel)
}

@Composable
private fun AddSpendingForm(
    upsertShoppingListViewModel: UpsertShoppingListViewModel,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        AddSpendingFormGeneralInfo(
            upsertShoppingListViewModel = upsertShoppingListViewModel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
        )
        CategoryShoppingList(
            categories = upsertShoppingListViewModel.shoppingListState.categories,
            onDeleteElement = { upsertShoppingListViewModel.removeSpending(it) },
            onSave = { upsertShoppingListViewModel.addSpending(it) },
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        )
    }
}

@Composable
private fun AddSpendingFormGeneralInfo(
    upsertShoppingListViewModel: UpsertShoppingListViewModel,
    modifier: Modifier = Modifier,
) {
    ConstructorBox(
        modifier = modifier,
        postModifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 30.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
            ) {
                Fonts.heading1.Text(text = "New shopping list")
            }

            Fonts.regular.Text(
                text = "Title",
                modifier = Modifier
                    .padding(top = 26.dp)
            )
            ConstructorInput(
                value = upsertShoppingListViewModel.shoppingListState.title,
                onValueChange = upsertShoppingListViewModel::setTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )

            Fonts.regular.Text(
                text = "Color",
                modifier = Modifier
                    .padding(top = 18.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                ShoppingListState.colors.forEach { color ->
                    val interactionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .let {
                                if (color.toArgb() == upsertShoppingListViewModel.shoppingListState.color) {
                                    it.border(2.dp, Colors.activationColor, CircleShape)
                                } else {
                                    it
                                }
                            }
                            .size(32.dp)
                            .background(color, CircleShape)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                            ) {
                                upsertShoppingListViewModel.setColor(color.toArgb())
                            }
                    )
                }
            }
        }
    }
}
