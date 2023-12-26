package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.finitas.app.core.presentation.components.constructors.Dropdown
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.CurrencyValues
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun CurrencyInput(viewModel: ProfileViewModel, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
    ) {
        Fonts.regular.Text(text = "Default currency")
        Dropdown(
            currentValue = viewModel.profileSettingsState.currency,
            values = CurrencyValues.entries,
            onClick = viewModel::setCurrency
        )
    }
}
