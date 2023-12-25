package pl.finitas.app.core.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.utils.colors.Colors

@Composable
fun ConstructorBoxDialog(
    isOpen: Boolean,
    onSave: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.BottomCenter,
    content: @Composable ColumnScope.() -> Unit,
) {
    NestedDialog(isOpen = isOpen, onClose = onClose) {
        val interactionSource = remember { MutableInteractionSource() }

        ConstructorBox(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp)
                .padding(horizontal = 10.dp)
                .background(Colors.backgroundLight, shape = RoundedCornerShape(10.dp))
                .align(alignment)
                .clickable(
                    interactionSource = interactionSource, indication = null
                ) {}
        ) {
            Column {
                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
                    content()
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ClickableIcon(
                        imageVector = Icons.Rounded.Close, onClick = onClose
                    )
                    ClickableIcon(imageVector = Icons.Rounded.Check, onClick = onSave)
                }
            }
        }
    }
}