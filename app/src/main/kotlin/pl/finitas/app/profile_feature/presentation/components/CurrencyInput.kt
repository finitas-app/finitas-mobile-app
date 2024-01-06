package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.Dropdown
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.CurrencyValue
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
        val currency by viewModel.currency.collectAsState(initial = CurrencyValue.PLN)
        Dropdown(
            currentValue = currency,
            values = CurrencyValue.entries,
            onClick = viewModel::setCurrency,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
}
