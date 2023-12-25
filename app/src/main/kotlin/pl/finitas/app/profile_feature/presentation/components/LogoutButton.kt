package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun LogoutButton(viewModel: ProfileViewModel, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
    ) {
        Fonts.regular.Text(text = "Logout", color = Color.Red)
        ClickableIcon(
            imageVector = Icons.Rounded.Logout,
            onClick = viewModel::logout,
            color = Color.Red,
            iconSize = 40.dp
        )
    }
}