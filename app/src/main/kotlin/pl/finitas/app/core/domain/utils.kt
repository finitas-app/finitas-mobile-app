package pl.finitas.app.core.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import pl.finitas.app.core.data.NOTIFICATION_CHANNEL_ID
import pl.finitas.app.core.data.NOTIFICATION_CHANNEL_NAME
import java.util.UUID

val emptyUUID: UUID = UUID.fromString("00000000-0000-4741-0000-000000000000")

fun notificationChannel() =
    NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    )
