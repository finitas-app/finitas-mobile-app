package pl.finitas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.inputs.ConstructorInput
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
            var text by remember { mutableStateOf("") }
            var text1 by remember { mutableStateOf("") }
            PrimaryBackground {
                Column(
                    modifier = Modifier.align(Alignment.Center)) {
                    ConstructorInput(
                        label = "Carrefour",
                        value = text,
                        onValueChange = { text = it },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ConstructorInput(
                        label = "Carrefour2",
                        value = text1,
                        onValueChange = { text1 = it },
                    )
                }
                Button(onClick = { /*TODO*/ }) {
                    
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