package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.CurrencyValues
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun CurrencyInput(viewModel: ProfileViewModel, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
    ) {
        Fonts.regular.Text(text = "Default currency")
        DropdownDemo(viewModel = viewModel)
    }
}

@Composable
private fun DropdownDemo(viewModel: ProfileViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            CurrencyTextInput(viewModel = viewModel)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(0.dp)
        ) {
            CurrencyValues.values().forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            it.name,
                            fontSize = 20.sp,
                            color = Color.White.copy(alpha = 0.3f),
                            textAlign = TextAlign.Justify
                        )
                    },
                    onClick = {
                        viewModel.setCurrency(it)
                        expanded = false
                    },
                    modifier = Modifier
                        .background(Colors.backgroundDark)
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)))
                )
            }
        }
    }
}

@Composable
private fun CurrencyTextInput(viewModel: ProfileViewModel) {
    Box(contentAlignment = Alignment.CenterEnd) {
        BasicTextField(
            onValueChange = {},
            value = viewModel.profileSettingsState.currency.name,
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.3f),
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier
                .height(35.dp)
                .width(70.dp)
                .background(Colors.backgroundDark, RoundedCornerShape(8.dp))
                .border(
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                    RoundedCornerShape(8.dp)
                )
                .padding(start = 12.dp)
                .wrapContentSize(Alignment.Center),
            singleLine = true,
            enabled = false,
        )
    }
}
