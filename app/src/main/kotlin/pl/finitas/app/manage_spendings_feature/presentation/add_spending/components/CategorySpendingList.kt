package pl.finitas.app.manage_spendings_feature.presentation.add_spending.components

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
import pl.finitas.app.core.domain.services.SpendingCategoryView
import pl.finitas.app.core.domain.services.SpendingElementView
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.Dropdown
import pl.finitas.app.core.presentation.components.constructors.LayeredList
import pl.finitas.app.core.presentation.components.dialog.NestedDialog
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import java.util.UUID

@Composable
fun CategorySpendingList(
    categories: List<SpendingCategoryView>,
    onDeleteElement: (SpendingRecordView) -> Unit,
    onSave: (SpendingRecordView) -> Unit,
    modifier: Modifier = Modifier,
) {
    var filterSearch by remember { mutableStateOf("") }
    var idSpendingCategory by remember { mutableStateOf<UUID?>(null) }
    var isOpenedAddSpendingRecord by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<SpendingRecordView?>(null) }
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

            LayeredList<SpendingElementView>(
                nameableCollection = categories.filter { it.name.contains(filterSearch) },
                modifier = Modifier.padding(top = 20.dp),
                itemExtras = { spendingElement ->
                    when (spendingElement) {
                        is SpendingCategoryView -> {
                            Box(modifier = Modifier.padding(end = 10.dp)) {
                                ClickableIcon(
                                    imageVector = Icons.Rounded.AddCircle, onClick = {
                                        idSpendingCategory = spendingElement.idCategory
                                        isOpenedAddSpendingRecord = true
                                    }, modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }

                        is SpendingRecordView -> {
                            Row(
                                modifier = Modifier.padding(end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Fonts.regular.Text(
                                    text = spendingElement.totalPrice.toString(),
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
                    if (element is SpendingRecordView) {
                        selected = element
                        idSpendingCategory = element.idCategory
                        isOpenedAddSpendingRecord = true
                    }
                }
            )

            AddSpendingRecordDialog(
                isOpen = isOpenedAddSpendingRecord,
                idCategory = idSpendingCategory ?: UUID.randomUUID(),
                onSave = {
                    selected?.let(onDeleteElement)
                    onSave(it)
                },
                categories = categories,
                spendingRecord = selected,
                onClose = {
                    selected = null
                    idSpendingCategory = null
                    isOpenedAddSpendingRecord = false
                },
            )
        }
    }
}

@Composable
private fun AddSpendingRecordDialog(
    isOpen: Boolean,
    idCategory: UUID,
    spendingRecord: SpendingRecordView?,
    categories: List<SpendingCategoryView>,
    onSave: (SpendingRecordView) -> Unit,
    onClose: () -> Unit,
) {
// TODO: rewrite to ConstructorBoxDialog()
    NestedDialog(isOpen = isOpen, onDismiss = onClose) {
        val categoriesById = remember(categories) {
            categories.associateBy { it.idCategory }
        }
        val interactionSource = remember { MutableInteractionSource() }

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
                    mutableStateOf(spendingRecord?.name ?: "")
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

                var category by remember {
                    mutableStateOf(spendingRecord?.idCategory ?: idCategory)
                }
                Fonts.regular.Text(
                    text = "Category", modifier = Modifier.padding(top = 10.dp)
                )
                Dropdown(
                    currentValue = categoriesById[category]!!,
                    values = categories,
                    onClick = { category = it.idCategory },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    mapToString = { it.name },
                    textAlign = Alignment.CenterStart
                )



                var totalPrice by remember {
                    mutableStateOf(spendingRecord?.totalPrice?.toString() ?: "")
                }
                Fonts.regular.Text(
                    text = "Total sum", modifier = Modifier.padding(top = 10.dp)
                )
                ConstructorInput(
                    value = totalPrice,
                    onValueChange = { totalPrice = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
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
                        onSave(
                            SpendingRecordView(
                                name = spendingTitle,
                                totalPrice = totalPrice.toBigDecimal(),
                                idCategory = category,
                                idSpendingRecord = UUID.randomUUID(),
                            )
                        )
                        onClose()
                    })
                }
            }
        }
    }
}