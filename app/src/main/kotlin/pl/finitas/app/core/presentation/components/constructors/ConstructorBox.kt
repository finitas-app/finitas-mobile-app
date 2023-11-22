package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ConstructorBox(
    modifier: Modifier = Modifier,
    brush: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF213138), Color(0xFF0D1016)
        ),
    ),
    postModifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(7.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .constructorBoxBackground(brush, shape)
            .then(postModifier)
    ) {
        content()
    }
}

fun Modifier.constructorBoxBackground(
    brush: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF213138), Color(0xFF0D1016)
        ),
    ),
    shape: Shape = RoundedCornerShape(7.dp),
) = background(
    brush = brush,
    shape = shape,
)