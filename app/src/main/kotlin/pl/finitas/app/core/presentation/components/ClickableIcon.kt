package pl.finitas.app.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ClickableIcon(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    contentDescription: String = "",
    iconSize: Dp = 32.dp,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription, tint = color, modifier = Modifier.size(iconSize))
    }
}

@Composable
fun ClickableIcon(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    contentDescription: String = "",
    iconSize: Dp = 32.dp,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(painter = painter, contentDescription = contentDescription, tint = color, modifier = Modifier.size(iconSize))
    }
}