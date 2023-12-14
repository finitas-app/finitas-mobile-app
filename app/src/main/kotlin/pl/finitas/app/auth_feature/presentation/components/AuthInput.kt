package pl.finitas.app.auth_feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun AuthInput(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    inputBorderColor: Brush = SolidColor(Color.White.copy(.1f)),
    onValueChange: (String) -> Unit = {},
) {
    Column (modifier){
        Fonts.heading2.Text(
            text = label,
            color = Color.White.copy(.5f),
            modifier = Modifier
                .padding(start = 20.dp, bottom = 10.dp)
        )
        AuthInput(
            value = value,
            onValueChange = onValueChange,
            inputBorderColor = inputBorderColor,
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}

@Composable
fun AuthInput(
    value: String,
    modifier: Modifier = Modifier,
    inputBorderColor: Brush,
    shape: Shape = RoundedCornerShape(8.dp),
    onValueChange: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 22.sp,
            color = Color.White.copy(.8f),
            textAlign = TextAlign.Justify
        ),
        modifier = Modifier
            .height(50.dp)
            .then(modifier)
            .width(IntrinsicSize.Min)
            .background(Colors.backgroundDark, shape)
            .border(1.dp, inputBorderColor, shape)
            .padding(start = 12.dp)
            .wrapContentSize(Alignment.CenterStart),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        cursorBrush = SolidColor(Color.White)
    )
}