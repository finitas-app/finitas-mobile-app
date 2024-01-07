package pl.finitas.app.profile_feature.domain.services

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.finitas.app.core.data.WorkerProcessTags
import pl.finitas.app.core.domain.services.NotificationService
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class ReminderNotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {

    val service: NotificationService by inject()

    @WorkerThread
    override fun doWork(): Result {
        runBlocking {
            service.pushRegularSpendingActualizationNotification()
        }

        return Result.success()
    }
}

class NotificationPusherService(private val context: Context) {
    private fun calculateDelay(desiredTime: LocalTime): Duration {
        val duration = Duration.between(LocalTime.now(), desiredTime)
        return if (duration.isNegative) duration.plusDays(1) else duration
    }

    fun launchNotifications(time: LocalTime) {
        val workerManager = WorkManager.getInstance(context)
        workerManager.cancelAllWorkByTag(WorkerProcessTags.REMINDER_NOTIFICATION.toString())
        val request = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(1, TimeUnit.DAYS)
            .addTag(WorkerProcessTags.REMINDER_NOTIFICATION.toString())
            .setInitialDelay(
                calculateDelay(time)
                    .also {
                        println("First notification will be pushed in $it")
                    }
            )
            .build()
        workerManager.enqueue(request)
    }

    fun cancelNotifications() {
        val workerManager = WorkManager.getInstance(context)
        workerManager.cancelAllWorkByTag(WorkerProcessTags.REMINDER_NOTIFICATION.toString())
    }
}