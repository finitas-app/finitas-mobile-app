package pl.finitas.app.core.presentation.components.spendingeditor.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pl.finitas.app.core.presentation.components.spendingeditor.calendar.YearMonthEditor
import java.time.LocalDate

@Composable
fun CalendarTest() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF213138))) {
        var date by remember {
            mutableStateOf(LocalDate.of(2003, 12, 2))
        }
        YearMonthEditor(
            date = date,
            onDateChange = { date = it },
            modifier = Modifier.align(Alignment.Center)
        )
    }
}