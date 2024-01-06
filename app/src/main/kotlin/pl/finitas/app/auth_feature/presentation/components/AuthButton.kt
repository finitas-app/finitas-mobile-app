package pl.finitas.app.auth_feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun AuthButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    isDisable: Boolean = false,
    showLoadingIcon: Boolean = false,
) {
    val borderShape = RoundedCornerShape(10.dp)
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier
            .background(Color(0x43, 0x55, 0xFA).copy(.5f), borderShape)
            .border(1.dp, Color.White.copy(.1f), borderShape)
            .clickable(
                onClick = { if (!isDisable) onClick() },
                indication = null,
                interactionSource = interactionSource,
            )
            .padding(vertical = 10.dp)
    ) {
        Box(
            Modifier
                .height(
                    with(LocalDensity.current) {
                        (Fonts.heading1.fontSize * 1.3).toDp()
                    }
                )
                .fillMaxWidth()
        ) {
            if (showLoadingIcon && isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxHeight().align(Alignment.Center),
                    trackColor = Colors.mirrorSpendingList,
                    color = Colors.backgroundLight,
                    strokeWidth = 4.dp
                )
            }else {
                Fonts.heading1.Text(
                    text = text,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
