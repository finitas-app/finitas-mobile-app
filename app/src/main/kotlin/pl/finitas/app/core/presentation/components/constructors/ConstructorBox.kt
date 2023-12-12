package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.colors.Colors

@Composable
fun ConstructorBox(
    modifier: Modifier = Modifier,
    brush: Brush = Brush.verticalGradient(
        colors = listOf(
            Colors.mirrorSpendingList, Colors.backgroundDark,
        ),
    ),
    postModifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(7.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .constructorBoxBackground(brush, shape)
            .clickable(interactionSource = interactionSource, indication = null, onClick = {})
            .then(postModifier)
    ) {
        content()
    }
}

@Composable
fun Modifier.constructorBoxBackground(
    brush: Brush = Brush.verticalGradient(
        colors = listOf(
            Colors.mirrorSpendingList, Colors.backgroundDark,
        ),
    ),
    shape: Shape = RoundedCornerShape(7.dp),
) = background(
    brush = brush,
    shape = shape,
)