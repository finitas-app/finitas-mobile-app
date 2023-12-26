package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun InputError(
    message: String?,
    modifier: Modifier = Modifier
) {
    if(message != null) {
        Fonts.regularMini.Text(
            text = "* $message",
            modifier = modifier.padding(top = 26.dp),
            color = Color.Red
        )
    }
}