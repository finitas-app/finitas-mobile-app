package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


@Composable
fun BoxScope.GestureVerticalMenu(
    topLimit: Float,
    bottomLimit: Float,
    content: @Composable ColumnScope.() -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val height = screenHeight - (topLimit * screenHeight)
    val topLimitFloat = 0f
    val bottomLimitFloat = (bottomLimit - topLimit) * LocalDensity.current.density * screenHeight.value


    var offsetY by remember { mutableFloatStateOf(0f) }
    var isPressed by remember { mutableStateOf(false) }
    var isTop by remember { mutableStateOf(false) }

    val position by animateFloatAsState(
        targetValue = run {
            if (isPressed) return@run offsetY

            offsetY = if (isTop && offsetY < bottomLimitFloat - 300) {
                topLimitFloat
            } else {
                bottomLimitFloat
            }
            offsetY
        },
        label = "position",
    )

    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset { IntOffset(0, position.roundToInt()) }
            .fillMaxWidth()
            .height(height)
            .constructorBoxBackground(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                )
            )
        ,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragStart = { isPressed = true },
                            onDragEnd = { isPressed = false },
                        ) { change, dragAmount ->
                            change.consume()
                            offsetY = (offsetY + dragAmount).let {
                                if (dragAmount > 0) {
                                    min(it, bottomLimitFloat)
                                } else {
                                    max(it, topLimitFloat)
                                }
                            }
                            isTop = dragAmount < 0
                            println("${change.pressed} - $offsetY")
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(vertical = 10.dp)
                        .width(40.dp)
                        .height(8.dp)
                        .background(Colors.backgroundDark, RoundedCornerShape(10.dp))
                )
            }
            content()
        }
    }
}