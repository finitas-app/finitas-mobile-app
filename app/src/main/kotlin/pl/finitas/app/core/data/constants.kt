package pl.finitas.app.core.data

import java.time.LocalTime

const val NOTIFICATION_CHANNEL_ID = "finitasNotificationChannel"
const val NOTIFICATION_CHANNEL_NAME = "Finitas"
const val MESSENGER_NOTIFICATION_TITLE = "Finitas Rooms"
val DEFAULT_REMINDER_NOTIFICATION_TIME: LocalTime = LocalTime.of(19, 0)
val DEFAULT_REGULAR_SPENDING_ACTUALIZATION_TIME: LocalTime = LocalTime.of(0, 0)

enum class WorkerProcessTags {
    REMINDER_NOTIFICATION,
    REGULAR_SPENDING_ACTUALIZATION
}

enum class NotificationType {
    REMINDER,
    REGULAR_SPENDING_UPDATE,
}

data class NotificationText(
    val title: String,
    val content: String,
)

val notificationTextMap = mapOf(
    NotificationType.REMINDER to NotificationText(
        title = "Finitas",
        content = "Do not forget to update your spendings"
    ),
    NotificationType.REGULAR_SPENDING_UPDATE to NotificationText(
        title = "Finitas",
        content = "Your regular spendings were updated."
    )
)