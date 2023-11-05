package pl.finitas.app.core.presentation.components.spendingeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.core.presentation.components.utils.colors.Colors

@Composable
fun ConstructorInput(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    rightIcon: (@Composable (Modifier) -> Unit)? = null,
) {
    val transparentWhite = Color.White.copy(alpha = 0.3f)
    val shape = RoundedCornerShape(8.dp)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = transparentWhite,
            textAlign = TextAlign.Justify
        ),
        modifier = modifier
            .background(Colors.backgroundDark, shape)
            .border(1.dp, transparentWhite, shape)
            .padding(start = 12.dp)
            .wrapContentSize(Alignment.CenterStart),
        singleLine = true,
        enabled = enabled,
    ) {
        if (rightIcon != null) {
            Box(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(end = 50.dp)
                ) {
                    it()
                }
                rightIcon(Modifier.align(Alignment.CenterEnd))
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().padding(end = 12.dp)) {
                it()
            }
        }
    }
}

@Preview
@Composable
fun ConstructorInputPreview() {

}
