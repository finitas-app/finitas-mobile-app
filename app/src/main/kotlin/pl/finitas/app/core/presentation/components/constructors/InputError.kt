package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun ColumnScope.InputError(
    errors: List<String>?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(errors != null) {
        Column(modifier) {
            errors?.forEach {
                Fonts.regularMini.Text(
                    text = "* $it",
                    color = Color.Red
                )
            }
        }
    }
}