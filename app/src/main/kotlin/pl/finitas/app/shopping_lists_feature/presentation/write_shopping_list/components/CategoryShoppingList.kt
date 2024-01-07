package pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.constructors.LayeredList
import pl.finitas.app.core.presentation.components.dialog.NestedDialog
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemCategoryView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListElement
import java.util.UUID

@Composable
fun CategoryShoppingList(
    categories: List<ShoppingItemCategoryView>,
    onDeleteElement: (ShoppingItemView) -> Unit,
    onSave: (ShoppingItemView) -> Unit,
    modifier: Modifier = Modifier,
) {
    var filterSearch by remember { mutableStateOf("") }
    var idSpendingCategory by remember { mutableStateOf<UUID?>(null) }
    var isOpenedAddSpendingRecord by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<ShoppingItemView?>(null) }

    ConstructorBox(
        modifier = modifier,
        postModifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 30.dp),
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF213138), Color(0xFF0D1016)
            ),
            startY = -300f,
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Fonts.heading1.Text(text = "List")
            }


            ConstructorInput(
                value = filterSearch,
                onValueChange = { filterSearch = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "searchIcon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            LayeredList<ShoppingListElement>(
                nameableCollection = categories.filter { it.name.contains(filterSearch) },
                modifier = Modifier.padding(top = 20.dp),
                itemExtras = { spendingElement ->
                    when (spendingElement) {
                        is ShoppingItemCategoryView -> {
                            Box(modifier = Modifier.padding(end = 10.dp)) {
                                ClickableIcon(
                                    imageVector = Icons.Rounded.AddCircle, onClick = {
                                        idSpendingCategory = spendingElement.idSpendingCategory
                                        isOpenedAddSpendingRecord = true
                                    }, modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }

                        is ShoppingItemView -> {
                            Row(
                                modifier = Modifier.padding(end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Fonts.regular.Text(
                                    text = spendingElement.amount.toString(),
                                    modifier = Modifier.padding(end = 30.dp)
                                )

                                ClickableIcon(
                                    imageVector = Icons.Rounded.Delete,
                                    onClick = { onDeleteElement(spendingElement) },
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        else -> {}
                    }
                },
                onNameClick = { element ->
                    if (element is ShoppingItemView) {
                        selected = element
                        idSpendingCategory = element.idSpendingCategory
                        isOpenedAddSpendingRecord = true
                    }
                }
            )

            AddSpendingRecordDialog(
                shoppingItemView = selected,
                isOpen = isOpenedAddSpendingRecord,
                idCategory = idSpendingCategory ?: UUID.randomUUID(),
                onSave = {
                    selected?.let(onDeleteElement)
                    onSave(it)
                },
                onClose = {
                    selected = null
                    idSpendingCategory = null
                    isOpenedAddSpendingRecord = false
                },
            )
        }
    }
}

private val amountRegex = "[0-9]{0,9}".toRegex()

@Composable
private fun AddSpendingRecordDialog(
    shoppingItemView: ShoppingItemView?,
    isOpen: Boolean,
    idCategory: UUID,
    onSave: (ShoppingItemView) -> Unit,
    onClose: () -> Unit,
) {
    NestedDialog(isOpen = isOpen, onDismiss = onClose) {
        val interactionSource = remember { MutableInteractionSource() }
        var titleErrors by remember { mutableStateOf<List<String>?>(null) }
        var amountErrors by remember { mutableStateOf<List<String>?>(null) }

        ConstructorBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp)
                .padding(horizontal = 10.dp)
                .background(Colors.backgroundLight, shape = RoundedCornerShape(10.dp))
                .align(Alignment.BottomCenter)
                .clickable(
                    interactionSource = interactionSource, indication = null
                ) {}, postModifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                ) {
                    Fonts.heading1.Text(text = "Add spending")
                }
                var spendingTitle by remember {
                    mutableStateOf(shoppingItemView?.name ?: "")
                }
                Fonts.regular.Text(
                    text = "Title", modifier = Modifier.padding(top = 20.dp)
                )
                ConstructorInput(
                    value = spendingTitle,
                    onValueChange = { spendingTitle = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
                InputError(errors = titleErrors, modifier = Modifier.padding(top = 10.dp))


                var amount by remember {
                    mutableStateOf(shoppingItemView?.amount?.toString() ?: "")
                }
                Fonts.regular.Text(
                    text = "Amount", modifier = Modifier.padding(top = 10.dp)
                )
                ConstructorInput(
                    value = amount,
                    onValueChange = {
                        if (it.matches(amountRegex)) {
                            amount = it.ifEmpty { null }?.toInt()?.toString() ?: ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
                InputError(errors = amountErrors, modifier = Modifier.padding(top = 10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ClickableIcon(
                        imageVector = Icons.Rounded.Close, onClick = onClose
                    )
                    ClickableIcon(imageVector = Icons.Rounded.Check, onClick = {
                        var hasErrors = false
                        if (spendingTitle.isBlank()) {
                            titleErrors = listOf(
                                "Title cannot be empty."
                            )
                            hasErrors = true
                        }
                        if (amount.isEmpty() || amount == "0") {
                            amountErrors = listOf(
                                "Amount should be positive."
                            )
                            hasErrors = true
                        }
                        if (!hasErrors) {
                            onSave(
                                ShoppingItemView(
                                    name = spendingTitle,
                                    amount = amount.ifBlank { "0" }.toInt(),
                                    idSpendingCategory = idCategory,
                                    idShoppingItem = UUID.randomUUID(),
                                )
                            )
                            onClose()
                        }
                    })
                }
            }
        }
    }
}