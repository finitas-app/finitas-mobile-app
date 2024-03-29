package pl.finitas.app.profile_feature.presentation

data class TimeState(
    val minutes: Int,
    val hours: Int,
)

data class RegularSpendingsSettingsState(
    val actualizationTime: TimeState,
    val actualizationNotificationsOn: Boolean
)

data class ReminderSettingsState(
    val notificationTime: TimeState,
    val isNotificationsOn: Boolean
)

data class ProfileSettingsState(
    val currency: CurrencyValue,
    val reminderSettingsState: ReminderSettingsState,
    val regularSpendingsSettingsState: RegularSpendingsSettingsState
) {
    companion object {
        val empty
            get() = ProfileSettingsState(
                currency = CurrencyValue.PLN,
                reminderSettingsState = ReminderSettingsState(
                    notificationTime = TimeState(0, 0),
                    isNotificationsOn = true
                ),
                regularSpendingsSettingsState = RegularSpendingsSettingsState(
                    actualizationNotificationsOn = true,
                    actualizationTime = TimeState(0, 0)
                )
            )
    }
}

enum class CurrencyValue {
    PLN,
    USD,
    EUR
}