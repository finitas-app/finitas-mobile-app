package pl.finitas.app.profile_feature.presentation.components

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import pl.finitas.app.core.data.DEFAULT_REMINDER_NOTIFICATION_TIME
import pl.finitas.app.core.presentation.components.constructors.SwitchComponent
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.core.presentation.workers.ReminderNotificationWorkerViewModel
import pl.finitas.app.profile_feature.presentation.NotificationPushViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReminderSettings(
    notificationPushViewModel: NotificationPushViewModel,
    reminderNotificationWorkerViewModel: ReminderNotificationWorkerViewModel,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        Fonts.regular.Text(
            text = "Reminders",
            modifier = Modifier.padding(bottom = 10.dp)
        )

        val innerModifier = Modifier.padding(top = 12.dp)
        NotificationsSwitch(
            notificationPushViewModel = notificationPushViewModel,
            modifier = innerModifier,
            reminderNotificationWorkerViewModel = reminderNotificationWorkerViewModel
        )
        NotificationTime(
            notificationPushViewModel = notificationPushViewModel,
            modifier = innerModifier,
            reminderNotificationWorkerViewModel = reminderNotificationWorkerViewModel
        )
    }
}

@Composable
private fun NotificationTime(
    notificationPushViewModel: NotificationPushViewModel,
    reminderNotificationWorkerViewModel: ReminderNotificationWorkerViewModel,
    modifier: Modifier
) {
    val reminderState by notificationPushViewModel.reminderPushState.collectAsState(initial = false)
    val reminderTime by notificationPushViewModel
        .reminderPushTime
        .collectAsState(initial = DEFAULT_REMINDER_NOTIFICATION_TIME)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Fonts.regularMini.Text(
            text = "Notification time",
            modifier = Modifier.padding(bottom = 6.dp),
            color = if (reminderState) Color.White else Color.Gray
        )
        TimeInput(
            time = reminderTime,
            onChange = {
                notificationPushViewModel.setReminderNotificationsTime(it)
                reminderNotificationWorkerViewModel.launchNotifications(it)
            },
            enabled = reminderState
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationsSwitch(
    notificationPushViewModel: NotificationPushViewModel,
    reminderNotificationWorkerViewModel: ReminderNotificationWorkerViewModel,
    modifier: Modifier
) {
    val reminderState by notificationPushViewModel.reminderPushState.collectAsState(initial = false)
    val postNotificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val reminderTime by notificationPushViewModel
        .reminderPushTime
        .collectAsState(initial = DEFAULT_REMINDER_NOTIFICATION_TIME)
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Fonts.regularMini.Text(text = "Notifications")
        SwitchComponent(
            isActive = reminderState,
            onChangeState = {
                if (it && !postNotificationPermission.status.isGranted) {
                    if (postNotificationPermission.status.shouldShowRationale) {
                        postNotificationPermission.launchPermissionRequest()
                    } else {
                        Toast.makeText(
                            context,
                            "Notifications are denied. Please allow notifications in settings",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }

                if (!it) {
                    notificationPushViewModel.setReminderNotificationsOn(false)
                    reminderNotificationWorkerViewModel.cancelNotifications()
                } else if (postNotificationPermission.status.isGranted) {
                    notificationPushViewModel.setReminderNotificationsOn(true)
                    reminderNotificationWorkerViewModel.launchNotifications(reminderTime)
                }
            }
        )
    }
}
