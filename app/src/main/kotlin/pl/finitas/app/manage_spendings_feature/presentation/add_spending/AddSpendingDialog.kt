package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.DateInput
import pl.finitas.app.core.presentation.components.dialog.CustomDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import java.time.LocalDate

@Composable
fun AddSpendingDialog(
    addSpendingViewModel: AddSpendingViewModel,
) {
    CustomDialog(
        isOpened = addSpendingViewModel.isDialogOpen.value,
    ) {
        AddSpendingPanel(addSpendingViewModel)
    }
}

@Composable
private fun AddSpendingPanel(
    addSpendingViewModel: AddSpendingViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF15171D).copy(.9f))
        ,
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
}

@Composable
private fun AddSpendingForm(
    addSpendingViewModel: AddSpendingViewModel,
) {
    ConstructorBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
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
                "Carrefour",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
            var date by remember {
                mutableStateOf(LocalDate.now())
            }

            Fonts.regular.Text(
                text = "Date",
                modifier = Modifier
                    .padding(top = 18.dp)
            )
            DateInput(
                date = date,
                onDateChange = { date = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}
