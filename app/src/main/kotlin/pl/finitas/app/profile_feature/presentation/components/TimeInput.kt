package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.TimeState

@Composable
fun TimeInput(
    timeState: TimeState,
    setMinutes: (value: Int) -> Unit,
    setHours: (value: Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.width(110.dp)
    ) {
        ConstructorInput(
            value = timeState.hours.toString(),
            onValueChange = {
                setHours(it.toIntOrNull()?.coerceIn(0, 23) ?: 0)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(50.dp),
        )

        Fonts.regular.Text(
            text = ":",
            color = Color.White,
            textAlign = TextAlign.Center
        )

        ConstructorInput(
            value = timeState.minutes.toString(),
            onValueChange = {
                setMinutes(it.toIntOrNull()?.coerceIn(0, 59) ?: 0)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(50.dp),
        )
    }
}