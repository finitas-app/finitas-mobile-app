package pl.finitas.app.core.presentation.components.spendingeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.R

@Composable
fun ConstructorInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val transparentWhite = Color.White.copy(alpha = 0.3f)
    val shape = RoundedCornerShape(8.dp)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Handle the "Done" action if needed
            }
        ),
        textStyle = TextStyle(fontSize = 20.sp, color = transparentWhite, textAlign = TextAlign.Justify),
        modifier = modifier
            .background(colorResource(R.color.background_dark), shape)
            .border(0.01.dp, transparentWhite, shape)
            .padding(horizontal = 12.dp)
            .wrapContentSize(Alignment.CenterStart),
        singleLine = true,
    )
}

@Preview
@Composable
fun ConstructorInputPreview() {

}
