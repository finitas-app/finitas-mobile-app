package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun UsernameInput(viewModel: ProfileViewModel, modifier: Modifier) {
    val username by viewModel.username.collectAsState(initial = "")
    var currentInput by remember(username) {
        mutableStateOf(username?: "")
    }
    Column(modifier = modifier) {
        Fonts.regular.Text(text = "Username", modifier = Modifier.padding(bottom = 6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            ConstructorInput(
                value = currentInput,
                onValueChange = { currentInput = it },
                borderStroke = null,
                modifier = Modifier
                    .weight(.9f)
                    .height(40.dp)
            )
            AnimatedVisibility(visible = username != currentInput ) {
                ClickableIcon(
                    imageVector = Icons.Rounded.Check,
                    onClick = { viewModel.setVisibleName(currentInput) },
                    iconSize = 32.dp,
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}