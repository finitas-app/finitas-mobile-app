package pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max


@Composable
fun ShoppingListBox(
    modifier: Modifier = Modifier,
    cornelRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    color: Color = Color(0xFFF85784),
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clippedPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            clipPath(clippedPath) {
                drawRoundRect(
                    brush = Brush.radialGradient(
                        listOf(
                            color,
                            color.copy(.2f)
                        ),
                        center = Offset(size.width * .1f, -size.height * .3f),
                        radius = max(size.height, size.width) * 1.3f
                    ),
                    size = size,
                    cornerRadius = CornerRadius(cornelRadius.toPx())
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornelRadius.toPx())
                )
            }
        }
        content()
    }
}