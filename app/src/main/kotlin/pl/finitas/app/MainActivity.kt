package pl.finitas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.spendingeditor.ConstructorBox
import pl.finitas.app.core.presentation.components.spendingeditor.ConstructorInput
import pl.finitas.app.ui.theme.FinitasmobileappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*FinitasmobileappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }*/
            ConstructorTest()
            //RoomsTest()
            //MessengerTest()
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
            ConstructorBox(
                modifier = Modifier
                    .padding(40.dp)
                    .height(500.dp)
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
            ConstructorBox(
                modifier = Modifier
                    .padding(40.dp),
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF213138),
                        Color(0xFF0D1016)
                    ),
                    startY = -700f
                ),
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinitasmobileappTheme {
        Greeting("Android")
    }
}

/*
* a = ?, b = ?, c = 4, S = 2
* s = 2 -> a * b = 4 -> b / 4 = a
* b(
*
* */