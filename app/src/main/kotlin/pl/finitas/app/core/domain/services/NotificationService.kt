package pl.finitas.app.core.domain.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import pl.finitas.app.R
import pl.finitas.app.core.data.MESSENGER_NOTIFICATION_TITLE
import pl.finitas.app.core.data.NOTIFICATION_CHANNEL_ID
import pl.finitas.app.core.data.NotificationType
import pl.finitas.app.core.data.notificationTextMap
import kotlin.random.Random

class NotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun pushMessengerNotification(content: String) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(MESSENGER_NOTIFICATION_TITLE)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_send_icon) // todo: choose icon
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }

    fun pushReminderNotification() = sendNotificationByType(NotificationType.REMINDER)

    fun pushRegularSpendingActualizationNotification() =
        sendNotificationByType(NotificationType.REGULAR_SPENDING_UPDATE)

    private fun sendNotificationByType(type: NotificationType) {
        val text = notificationTextMap[type]!!
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(text.title)
            .setContentText(text.content)
            .setSmallIcon(R.drawable.ic_send_icon) // todo: choose icon
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
}