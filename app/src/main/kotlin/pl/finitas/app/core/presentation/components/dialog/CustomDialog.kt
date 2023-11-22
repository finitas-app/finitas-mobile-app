package pl.finitas.app.core.presentation.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun CustomDialog(
    isOpened: Boolean,
    onDismissRequest: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    if (isOpened) {
        val interactionSourceOuter = remember { MutableInteractionSource() }

        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSourceOuter,
                indication = null
            ) { onDismissRequest() }) {
            content()
        }
    }
}

