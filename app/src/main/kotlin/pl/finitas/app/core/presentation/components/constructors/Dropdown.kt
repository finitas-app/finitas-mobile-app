package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts

val textColor = Color.White.copy(alpha = 0.3f)
val borderStroke = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
val borderShape = RoundedCornerShape(8.dp)

@Composable
fun <T : Any> Dropdown(
    currentValue: T,
    values: List<T>,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    mapToString: (T) -> String = Any::toString,
    textAlign: Alignment = Alignment.Center,
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = modifier) {
        CurrentValueInput(
            value = mapToString(currentValue),
            textAlign,
            Modifier
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                ) { expanded = !expanded }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            values.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            mapToString(it),
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Justify
                        )
                    },
                    onClick = {
                        onClick(it)
                        expanded = false
                    },
                    modifier = Modifier
                        .background(Colors.backgroundDark)
                        .border(borderStroke)
                )
            }
        }
    }
}

@Composable
private fun CurrentValueInput(
    value: String,
    textAlign: Alignment,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(35.dp)
            .fillMaxWidth()
            .background(Colors.backgroundDark, borderShape)
            .border(
                border = borderStroke,
                shape = borderShape,
            ),
    ) {
        Fonts.regular.Text(
            text = value,
            color = textColor,
            modifier = Modifier.align(textAlign).padding(horizontal = 12.dp)
        )
    }
}