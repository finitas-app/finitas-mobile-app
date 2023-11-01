package pl.finitas.app.core.presentation.components.spendingeditor.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import pl.finitas.app.R
import pl.finitas.app.core.presentation.components.spendingeditor.calendar.Calendar
import java.time.LocalDate

@Composable
fun CalendarTest() {
    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.mirror_spending_list))
    ) {
        var date by remember {
            mutableStateOf(LocalDate.of(2003, 12, 2))
        }
        Text(text = "$date")
        Calendar(date, { date = it}, Modifier.align(Alignment.Center))
    }
}