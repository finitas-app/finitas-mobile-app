package pl.finitas.app.core.data

import java.time.LocalTime

const val NOTIFICATION_CHANNEL_ID = "finitasNotificationChannel"
const val NOTIFICATION_CHANNEL_NAME = "Finitas"
const val MESSENGER_NOTIFICATION_TITLE = "Finitas Rooms"
const val REMINDER_NOTIFICATION_TITLE = "Finitas Reminder"
val DEFAULT_REMINDER_NOTIFICATION_TIME: LocalTime = LocalTime.of(19, 0)
val DEFAULT_REGULAR_SPENDING_ACTUALIZATION_TIME: LocalTime = LocalTime.of(0, 0)

enum class WorkerProcessTags {
    REMINDER_NOTIFICATION,
    REGULAR_SPENDING_ACTUALIZATION
}

