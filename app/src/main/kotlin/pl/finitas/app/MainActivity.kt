package pl.finitas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.DateInput
import pl.finitas.app.core.presentation.components.constructors.GestureVerticalMenu
import pl.finitas.app.core.presentation.components.constructors.constructorBoxBackground
import pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending.RegularSpendingActualizationViewModel
import pl.finitas.app.navigation.AppNavigation
import pl.finitas.app.sync_feature.SynchronizationViewModel
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoinAndroidContext {
                koinViewModel<SynchronizationViewModel>()
                koinViewModel<RegularSpendingActualizationViewModel>()
                AppNavigation()
            }
            /*FinitasmobileappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }*/
            //ConstructorTest()
            //RoomsTest()
            //MessengerTest()
            //CalendarTest()
            //TestMovable()
            //SpendingListTest()
        }
    }
}


@Composable
fun TestMovable() {
    PrimaryBackground {
        GestureVerticalMenu(
            topLimit = 0.25f,
            bottomLimit = .75f,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                var date by remember { mutableStateOf(LocalDate.now()) }

                repeat(10) {
                    DateInput(
                        date = date,
                        onDateChange = { date = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun ConstructorTest() {
    var text by remember { mutableStateOf("") }
    var text1 by remember { mutableStateOf("") }
    PrimaryBackground {
        Column(
            Modifier
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .padding(40.dp)
                    .height(500.dp)
                    .constructorBoxBackground()
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    ConstructorInput(
                        value = text,
                        onValueChange = { text = it },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ConstructorInput(
                        value = text1,
                        onValueChange = { text1 = it },
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(40.dp)
                    .constructorBoxBackground(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF213138),
                                Color(0xFF0D1016)
                            ),
                            startY = -700f
                        )
                    )
                ,
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    ConstructorInput(
                        value = text,
                        onValueChange = { text = it },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ConstructorInput(
                        value = text1,
                        onValueChange = { text1 = it },
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

/*
* a = ?, b = ?, c = 4, S = 2
* s = 2 -> a * b = 4 -> b / 4 = a
* b(
*
* */