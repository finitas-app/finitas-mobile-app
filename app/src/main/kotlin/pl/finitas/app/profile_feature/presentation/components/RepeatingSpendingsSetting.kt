package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.data.DEFAULT_REGULAR_SPENDING_ACTUALIZATION_TIME
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.core.presentation.workers.RegularSpendingActualizationViewModel
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun RepeatingSpendingsSetting(
    profileViewModel: ProfileViewModel,
    regularSpendingActualizationViewModel: RegularSpendingActualizationViewModel,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        Fonts.regular.Text(
            text = "Repeating spendings",
            modifier = Modifier.padding(bottom = 10.dp)
        )

        val innerModifier = Modifier.padding(top = 12.dp)
        ActualizationTime(
            profileViewModel = profileViewModel,
            modifier = innerModifier,
            regularSpendingActualizationViewModel = regularSpendingActualizationViewModel
        )
    }
}

@Composable
private fun ActualizationTime(
    profileViewModel: ProfileViewModel,
    regularSpendingActualizationViewModel: RegularSpendingActualizationViewModel,
    modifier: Modifier
) {
    val timeValue by profileViewModel
        .regularSpendingActualizationTime
        .collectAsState(initial = DEFAULT_REGULAR_SPENDING_ACTUALIZATION_TIME)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Fonts.regularMini.Text(
            text = "Actualization time",
            modifier = Modifier.padding(bottom = 6.dp)
        )
        TimeInput(
            time = timeValue,
            onChange = {
                profileViewModel.setRegularSpendingActualizationTime(value = it)
                regularSpendingActualizationViewModel.launchActualization(time = it)
            }
        )
    }
}
