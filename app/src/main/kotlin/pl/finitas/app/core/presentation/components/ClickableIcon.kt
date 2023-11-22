package pl.finitas.app.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun ClickableIcon(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    contentDescription: String = "",
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription, tint = color)
    }
}