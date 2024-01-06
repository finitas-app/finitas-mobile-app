package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.core.presentation.components.utils.colors.Colors

private val transparentWhite = Color.White.copy(alpha = 0.3f)
private val placeholderColor = Color.White.copy(alpha = 0.2f)
private val borderColor = Color.White.copy(alpha = 0.1f)

// todo: make input higher
// todo: make cursor white
// todo: allow horizontal alignment for input field

@Composable
fun ConstructorInput(
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onValueChange: (String) -> Unit = {},
    postModifier: Modifier = Modifier,
    borderStroke: BorderStroke? = BorderStroke(1.dp, borderColor),
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions? = null,
    textAlign: TextAlign = TextAlign.Justify,
    rightIcon: (@Composable RowScope.() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(8.dp)
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value.ifEmpty { placeholder },
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = if (value.isEmpty()) placeholderColor else transparentWhite,
            textAlign = textAlign,
        ),
        modifier = Modifier
            .height(35.dp)
            .then(modifier)
            .width(IntrinsicSize.Min)
            .background(Colors.backgroundDark, shape)
            .let {
                if (borderStroke != null) {
                    it.border(borderStroke, shape)
                } else {
                    it
                }
            }
            .padding(start = 12.dp)
            .wrapContentSize(Alignment.CenterStart)
            .then(postModifier),
        singleLine = true,
        enabled = enabled,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    ) {
        if (rightIcon != null) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 50.dp)
                ) {
                    it()
                }
                rightIcon()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
            ) {
                it()
            }
        }
    }
}

@Composable
fun ConstructorInput(
    value: UInt,
    onValueChange: (UInt) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstructorInput(
        value = value.toString(),
        onValueChange = { onValueChange(it.toUIntOrNull() ?: 0.toUInt()) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}
