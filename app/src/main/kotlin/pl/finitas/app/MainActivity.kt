package pl.finitas.app

import android.Manifest
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.finitas.app.core.domain.notificationChannel
import pl.finitas.app.core.presentation.workers.RegularSpendingActualizationViewModel
import pl.finitas.app.navigation.AppNavigation
import pl.finitas.app.core.presentation.workers.ReminderNotificationWorkerViewModel
import pl.finitas.app.sync_feature.SynchronizationViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun AppNotifications() {
    val postNotificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoinAndroidContext {
                koinViewModel<SynchronizationViewModel>()
                koinViewModel<ReminderNotificationWorkerViewModel>()
                koinViewModel<RegularSpendingActualizationViewModel>()
                AppNavigation()
                AppNotifications()
            }
        }

        setupNotificationChannel()
    }

    private fun setupNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationChannel()
            .let(notificationManager::createNotificationChannel)
    }
}
