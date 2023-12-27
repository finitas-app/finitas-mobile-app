package pl.finitas.app.room_feature.presentation.messanger.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.utils.colors.Colors

@Composable
fun MessengerInput(
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPinObject: () -> Unit = {},
) {
    var value by remember { mutableStateOf("") }
    val inputColor = Colors.messageInputColor



    BasicTextField(
        value = value,
        onValueChange = { value = it },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Justify
        ),
        modifier = modifier
            .background(
                color = inputColor,
                shape = RoundedCornerShape(17.dp),
            )
            .padding(start = 10.dp)
            .wrapContentSize(align = Alignment.CenterStart),
    ) {
        Box(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 50.dp)
            ) {
                it()
            }
            if (value.isNotEmpty()) {
                ClickableIcon(
                    imageVector = Icons.Rounded.Send,
                    onClick = {
                        onSendMessage(value)
                        value = ""
                    },
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            } else {
                ClickableIcon(
                    imageVector = Icons.Rounded.Share,
                    onClick = {
                        onPinObject()
                    },
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        }
    }
}
