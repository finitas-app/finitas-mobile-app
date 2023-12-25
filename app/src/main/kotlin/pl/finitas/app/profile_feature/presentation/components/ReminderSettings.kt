package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.SwitchComponent
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

@Composable
fun ReminderSettings(viewModel: ProfileViewModel, modifier: Modifier) {
    Column(modifier = modifier) {
        Fonts.regular.Text(
            text = "Reminders",
            modifier = Modifier.padding(bottom = 10.dp)
        )

        val innerModifier = Modifier.padding(top = 12.dp)
        NotificationsSwitch(viewModel = viewModel, modifier = innerModifier)
        NotificationTime(viewModel = viewModel, modifier = innerModifier)
    }
}

@Composable
private fun NotificationTime(viewModel: ProfileViewModel, modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Fonts.regularMini.Text(
            text = "Notification time",
            modifier = Modifier.padding(bottom = 6.dp),
            color = if (viewModel
                    .profileSettingsState
                    .reminderSettingsState
                    .isNotificationsOn
            ) Color.White else Color.Gray
        )
        TimeInput(
            timeState = viewModel
                .profileSettingsState
                .reminderSettingsState
                .notificationTime,
            setHours = viewModel::setReminderNotificationsTimeHours,
            setMinutes = viewModel::setReminderNotificationsTimeMinutes
        )
    }
}

@Composable
private fun NotificationsSwitch(viewModel: ProfileViewModel, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Fonts.regularMini.Text(text = "Notifications")
        SwitchComponent(
            isActive = viewModel
                .profileSettingsState
                .reminderSettingsState
                .isNotificationsOn,
            onChangeState = viewModel::setReminderNotificationsOn
        )
    }
}
