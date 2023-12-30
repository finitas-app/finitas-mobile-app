package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun UsernameInput(viewModel: ProfileViewModel, modifier: Modifier) {
    val username by viewModel.username.collectAsState(initial = "")
    Column(modifier = modifier) {
        Fonts.regular.Text(text = "Username", modifier = Modifier.padding(bottom = 6.dp))
        ConstructorInput(
            value = username ?: "",
            onValueChange = viewModel::setVisibleName,
            borderStroke = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )
    }
}