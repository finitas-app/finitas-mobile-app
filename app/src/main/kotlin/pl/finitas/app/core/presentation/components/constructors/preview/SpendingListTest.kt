package pl.finitas.app.core.presentation.components.constructors.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.constructors.GestureVerticalMenu

@Preview
@Composable
fun SpendingListTest() {
    PrimaryBackground {
        GestureVerticalMenu(
            topLimit = 0f,
            bottomLimit = 0f,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier
                    .size(50.dp)
                    .background(Color.Red)
                    .align(Alignment.BottomCenter))
            }
        }
    }
}