package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun InputError(
    errors: List<String>?,
    modifier: Modifier = Modifier
) {
    if (errors != null) {
        Column(modifier.padding(top = 26.dp)) {
            errors.forEach {
                Fonts.regularMini.Text(
                    text = "* $it",
                    color = Color.Red
                )
            }
        }
    }
}