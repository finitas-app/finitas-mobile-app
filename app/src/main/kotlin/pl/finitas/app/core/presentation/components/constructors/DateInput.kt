package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.calendar.Calendar
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import java.time.LocalDate

@Composable
fun DateInput(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var calendarIsOpened by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.width(IntrinsicSize.Min)) {
        ConstructorInput(
            value = if(enabled) "$date" else "",
            enabled = enabled,
            rightIcon = {
                IconButton(
                    onClick = { if(enabled) calendarIsOpened = !calendarIsOpened },
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = "",
                        tint = if(!enabled) {
                            Color.Gray
                        } else {
                            if (calendarIsOpened) Colors.activationColor else Color.White
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility (
            visible = calendarIsOpened && enabled,
        ) {
            Calendar(
                date = date,
                setDate = onDateChange,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(285.dp)
            )
        }
    }
}