package pl.finitas.app.profile_feature.data

const val NOTIFICATION_CHANNEL_ID = "finitasNotificationChannel"
const val NOTIFICATION_CHANNEL_NAME = "Finitas"
const val MESSENGER_NOTIFICATION_TITLE = "Finitas Rooms"

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