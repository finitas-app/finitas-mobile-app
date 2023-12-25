package pl.finitas.app.manage_additional_elements_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun AdditionalElemHeader(
    title: String,
    addIconOnClick: () -> Unit,
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ){
        Fonts.heading1.Text(text = title)
        ClickableIcon(
            imageVector = Icons.Rounded.AddCircle,
            onClick = addIconOnClick,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}