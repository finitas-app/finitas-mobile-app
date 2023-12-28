package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.core.presentation.components.utils.text.ProjectFont

@Composable
fun DeleteIcon(
    label: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    projectFont: ProjectFont = Fonts.regular,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        ClickableIcon(
            imageVector = Icons.Rounded.RemoveCircleOutline,
            onClick = onDeleteClick,
            color = Colors.removeColor,
        )
        projectFont.Text(text = label, color = Colors.removeColor)
    }
}
