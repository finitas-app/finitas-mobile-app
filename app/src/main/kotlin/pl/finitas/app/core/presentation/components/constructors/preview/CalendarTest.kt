package pl.finitas.app.core.presentation.components.constructors.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.DateInput
import pl.finitas.app.core.presentation.components.constructors.constructorBoxBackground
import java.time.LocalDate

@Composable
fun CalendarTest() {
    PrimaryBackground {
        Box(
            modifier = Modifier
                .padding(top = 150.dp)
                .align(Alignment.TopCenter)
        ) {
            Box(
                Modifier
                    .height(600.dp)
                    .constructorBoxBackground()
            ) {
                var date by remember {
                    mutableStateOf(LocalDate.now())
                }
                var date2 by remember {
                    mutableStateOf(LocalDate.now())
                }
                var s by remember {
                    mutableStateOf("")
                }
                LazyColumn {
                    item {
                        DateInput(date = date, onDateChange = { date = it }, modifier = Modifier.padding(top = 10.dp))
                    }
                    item {
                        DateInput(date = date2, onDateChange = { date2 = it }, modifier = Modifier.padding(top = 10.dp))
                    }
                    item {
                        ConstructorInput(
                            value = s,
                            onValueChange = { s = it },
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                .width(290.dp)
                        )

                    }
                }
            }
        }
    }
}