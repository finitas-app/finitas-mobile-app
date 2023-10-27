package pl.finitas.app.core.presentation.components.utils.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Fonts {
    val heading1 = ProjectFont(28, FontWeight.SemiBold)
    val heading2 = ProjectFont(24, FontWeight.SemiBold)
    val regular = ProjectFont(20, FontWeight.Normal)
    val graphMini = ProjectFont(12, FontWeight.Normal)
    val chatMessages = ProjectFont(14, FontWeight.Normal)
    val chatMessagesHeader = ProjectFont(14, FontWeight.SemiBold)
    val chatMessageTime = ProjectFont(12, FontWeight.Normal)
    val regularMini = ProjectFont(16, FontWeight.Normal)
    val chatListHeader = ProjectFont(16, FontWeight.Normal)
}

data class ProjectFont(
    val fontSize: TextUnit,
    val fontWeight: FontWeight,
) {
    constructor(size: Int, fontWeight: FontWeight): this(size.sp, fontWeight)

    @Composable
    fun Text(text: String, modifier: Modifier = Modifier, color: Color = Color.White) {
        Text(
            text = text,
            modifier = modifier,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color,
        )
    }
}

@Preview
@Composable
private fun FontTest() {
    Box(
        Modifier
            .background(Color.White)
            .size(150.dp)) {
        Fonts.regular.Text(text = "123", modifier = Modifier.align(Alignment.Center))
    }
}