package pl.finitas.app.core.presentation.components.background

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


fun getPrimaryBackgroundColor(radius: Float): Brush {
    return Brush.radialGradient(
        colors = listOf(Color(89, 89, 98), Color(16, 16, 16)),
        center = Offset(30f, 30F),
        radius = radius
    )
}

val secondaryBackgroundColor = Color(0xFF0D1016)

@Composable
fun PrimaryBackground(
    modifier: Modifier = Modifier,
    isDialogOpen: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    val radius = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx() * 0.9f
    }
    val animatedBlur by animateDpAsState(
        targetValue = if (isDialogOpen) 15.dp else 0.dp,
        label = "blurAnimation",
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getPrimaryBackgroundColor(radius))
            .blur(animatedBlur)
            .then(modifier)
    ) {
        content()
    }
}

@Composable
fun SecondaryBackground(
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(secondaryBackgroundColor)
    ) {
        content()
    }
}

@Preview(name = "123", device = "id:pixel_6")
@Composable
fun test() {
    PrimaryBackground {
        Text(text = "112323")
    }
}