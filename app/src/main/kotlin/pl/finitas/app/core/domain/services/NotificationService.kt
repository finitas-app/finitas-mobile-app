package pl.finitas.app.core.domain.services

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import pl.finitas.app.R
import pl.finitas.app.core.data.MESSENGER_NOTIFICATION_TITLE
import pl.finitas.app.core.data.NOTIFICATION_CHANNEL_ID
import pl.finitas.app.core.data.REMINDER_NOTIFICATION_TITLE
import kotlin.random.Random

class NotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    // todo: use for messenger
    fun pushMessengerNotification(content: String) = sendNotification(
        title = MESSENGER_NOTIFICATION_TITLE,
        content = content
    )

    fun pushReminderNotification() = sendNotification(
        title = REMINDER_NOTIFICATION_TITLE,
        content = "Do not forget to update your spendings",
    )

    private fun sendNotification(title: String, content: String) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_send_icon)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(
                        context.bitmapFromResource(
                            R.drawable.ic_send_icon
                        )
                    )
            )
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

    private fun Context.bitmapFromResource(
        @DrawableRes resId: Int
    ) = BitmapFactory.decodeResource(
        resources,
        resId
    )
}