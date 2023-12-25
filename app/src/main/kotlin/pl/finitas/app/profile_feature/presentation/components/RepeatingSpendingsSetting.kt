package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.SwitchComponent
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun RepeatingSpendingsSetting(viewModel: ProfileViewModel, modifier: Modifier) {
    Column(modifier = modifier) {
        Fonts.regular.Text(
            text = "Repeating spendings",
            modifier = Modifier.padding(bottom = 10.dp)
        )

        val innerModifier = Modifier.padding(top = 12.dp)
        ActualizationTime(viewModel = viewModel, modifier = innerModifier)
        ActualizationNotifications(viewModel = viewModel, modifier = innerModifier)
    }
}

@Composable
private fun ActualizationTime(viewModel: ProfileViewModel, modifier: Modifier) {
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
            timeState = viewModel
                .profileSettingsState
                .regularSpendingsSettingsState
                .actualizationTime,
            setHours = viewModel::setRegularSpendingActualizationTimeHours,
            setMinutes = viewModel::setRegularSpendingActualizationTimeMinutes
        )
    }
}

@Composable
private fun ActualizationNotifications(viewModel: ProfileViewModel, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Fonts.regularMini.Text(text = "Notifications")
        SwitchComponent(
            isActive = viewModel.profileSettingsState.regularSpendingsSettingsState.actualizationNotificationsOn,
            onChangeState = viewModel::setRegularSpendingActualizationNotificationsOn
        )
    }
}
