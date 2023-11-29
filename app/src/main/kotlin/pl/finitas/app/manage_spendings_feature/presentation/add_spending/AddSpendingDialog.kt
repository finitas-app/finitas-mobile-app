package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.DateInput
import pl.finitas.app.core.presentation.components.constructors.SpendingList
import pl.finitas.app.core.presentation.components.dialog.CustomDialog
import pl.finitas.app.core.presentation.components.dialog.NestedDialog
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryView
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingRecordView

@Composable
fun AddSpendingDialog(
    addSpendingViewModel: AddSpendingViewModel,
) {
    CustomDialog(
        isOpened = addSpendingViewModel.isDialogOpen,
    ) {
        AddSpendingPanel(addSpendingViewModel)
    }
}

@Composable
private fun AddSpendingPanel(
    addSpendingViewModel: AddSpendingViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF595962).copy(.19f))
            .padding(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            ClickableIcon(
                imageVector = Icons.Rounded.Close,
                onClick = addSpendingViewModel::closeDialog
            )
            ClickableIcon(imageVector = Icons.Rounded.Check, onClick = {})
        }
        AddSpendingForm(addSpendingViewModel = addSpendingViewModel)
    }
}

@Composable
private fun AddSpendingForm(
    addSpendingViewModel: AddSpendingViewModel,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        AddSpendingFormGeneralInfo(
            addSpendingViewModel = addSpendingViewModel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
        )
        CategorySpendingList(
            addSpendingViewModel = addSpendingViewModel,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        )
    }
}

@Composable
private fun AddSpendingFormGeneralInfo(
    addSpendingViewModel: AddSpendingViewModel,
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
                Fonts.heading1.Text(text = "New report")
            }

            Fonts.regular.Text(
                text = "Title",
                modifier = Modifier
                    .padding(top = 40.dp)
            )
            ConstructorInput(
                value = addSpendingViewModel.totalSpendingState.title,
                onValueChange = addSpendingViewModel::setTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )

            Fonts.regular.Text(
                text = "Date",
                modifier = Modifier
                    .padding(top = 18.dp)
            )
            DateInput(
                date = addSpendingViewModel.totalSpendingState.date,
                onDateChange = addSpendingViewModel::setDate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun CategorySpendingList(
    addSpendingViewModel: AddSpendingViewModel,
    modifier: Modifier = Modifier,
) {
    var filterSearch by remember { mutableStateOf("") }
    var idSpendingCategory by remember { mutableIntStateOf(-1) }
    var isOpenedAddSpendingRecord by remember { mutableStateOf(false) }

    ConstructorBox(
        modifier = modifier,
        postModifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 30.dp),
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF213138), Color(0xFF0D1016)
            ),
            startY = -300f,
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
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

            SpendingList(
                spendingElements = addSpendingViewModel
                    .totalSpendingState
                    .categories
                    .filter { it.name.contains(filterSearch) },
                modifier = Modifier
                    .padding(top = 20.dp)
            ) { spendingElement ->
                when (spendingElement) {
                    is SpendingCategoryView -> {
                        Box(modifier = Modifier.padding(end = 10.dp)) {
                            ClickableIcon(
                                imageVector = Icons.Rounded.AddCircle,
                                onClick = {
                                    idSpendingCategory = spendingElement.idCategory
                                    isOpenedAddSpendingRecord = true
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    is SpendingRecordView -> {
                        Row(
                            modifier = Modifier
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Fonts.regular.Text(
                                text = spendingElement.totalPrice.toString(),
                                modifier = Modifier
                                    .padding(end = 30.dp)
                            )

                            ClickableIcon(
                                imageVector = Icons.Rounded.Delete,
                                onClick = { addSpendingViewModel.removeSpending(spendingElement) },
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        }
                    }

                    else -> {}
                }
            }

            AddSpendingRecordDialog(
                isOpen = isOpenedAddSpendingRecord,
                idCategory = idSpendingCategory,
                onSave = { addSpendingViewModel.addSpending(it) },
                onClose = {
                    idSpendingCategory = -1
                    isOpenedAddSpendingRecord = false
                },
            )
        }
    }
}

@Composable
private fun AddSpendingRecordDialog(
    isOpen: Boolean,
    idCategory: Int,
    onSave: (SpendingRecordView) -> Unit,
    onClose: () -> Unit,
) {
    NestedDialog(isOpen = isOpen, onClose = onClose) {
        val interactionSource = remember { MutableInteractionSource() }

        ConstructorBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp)
                .padding(horizontal = 10.dp)
                .background(Colors.backgroundLight, shape = RoundedCornerShape(10.dp))
                .align(Alignment.BottomCenter)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {},
            postModifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                ) {
                    Fonts.heading1.Text(text = "Add spending")
                }
                var spendingTitle by remember {
                    mutableStateOf("")
                }
                Fonts.regular.Text(
                    text = "Title",
                    modifier = Modifier
                        .padding(top = 20.dp)
                )
                ConstructorInput(
                    value = spendingTitle,
                    onValueChange = { spendingTitle = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )


                var totalPrice by remember {
                    mutableStateOf("")
                }
                Fonts.regular.Text(
                    text = "Total sum",
                    modifier = Modifier
                        .padding(top = 10.dp)
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
                        imageVector = Icons.Rounded.Close,
                        onClick = onClose
                    )
                    ClickableIcon(imageVector = Icons.Rounded.Check, onClick = {
                        onSave(
                            SpendingRecordView(
                                name = spendingTitle,
                                totalPrice = totalPrice.toBigDecimal(),
                                idCategory = idCategory,
                            )
                        )
                        onClose()
                    })
                }
            }
        }
    }
}
