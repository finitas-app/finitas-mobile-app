package pl.finitas.app.core.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon

@Composable
fun CustomDialog(
    isOpened: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (isOpened) {
        val interactionSourceOuter = remember { MutableInteractionSource() }
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSourceOuter,
                indication = null
            ) { onDismissRequest() }) {
            val innerInteractionSource = remember { MutableInteractionSource() }
            Column(
                modifier = modifier
                    .clickable(
                        interactionSource = innerInteractionSource,
                        indication = null,
                        onClick = {}
                    )
                    .align(alignment)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF595962).copy(.19f))
                    .padding(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    ClickableIcon(
                        imageVector = Icons.Rounded.Close,
                        onClick = onDismissRequest
                    )
                    ClickableIcon(imageVector = Icons.Rounded.Check, onClick = onConfirmRequest)
                }
                content()
            }
        }
    }
}

