package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.colors.Colors

// todo: add inner shadow

@Composable
fun SwitchComponent(
    isActive: Boolean,
    onChangeState: (Boolean) -> Unit,
    height: Dp = 32.dp,
) {
    val colorToggle by animateColorAsState(
        targetValue = if (isActive) Colors.activationColor else Colors.backgroundDark.copy(.5f),
        label = "colorToggle",
    )
    val colorToggleBackground by animateColorAsState(
        targetValue = if (isActive) Colors.activationColor.copy(.5f) else Color.White.copy(.2f),
        label = "colorToggle",
    )
    val toggleDiameter = height - 4.dp
    val position by animateDpAsState(
        targetValue = if (isActive) toggleDiameter else 0.dp,
        label = "position"
    )
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box(
        Modifier
            .size((height * 2) - 4.dp, height)
            .background(colorToggleBackground, RoundedCornerShape(100))
            .padding(2.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) { onChangeState(!isActive) }
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            val toggleRadiusPx = (toggleDiameter / 2).toPx()
            drawCircle(colorToggle, toggleRadiusPx, center.copy(x = center.x + toggleRadiusPx + position.toPx()))
        }
    }

}