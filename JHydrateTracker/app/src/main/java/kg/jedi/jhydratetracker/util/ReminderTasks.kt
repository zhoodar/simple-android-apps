package kg.jedi.jhydratetracker.util

import android.content.Context
import kg.jedi.jhydratetracker.Actions.ACTION_DISMISS_NOTIFICATION
import kg.jedi.jhydratetracker.Actions.ACTION_HYDRATE_REMINDER
import kg.jedi.jhydratetracker.Actions.ACTION_INCREMENT_WATER_COUNT


fun executeTask(context: Context, action: String) = when (action) {
    ACTION_INCREMENT_WATER_COUNT -> incrementWaterCount(context)
    ACTION_DISMISS_NOTIFICATION -> NotificationUtils.clearAllNotifications(context)
    ACTION_HYDRATE_REMINDER -> issueHydrateReminder(context)
    else -> {
    }
}

private fun incrementWaterCount(context: Context) {
    PreferenceUtils.incrementWaterCount(context)
    NotificationUtils.clearAllNotifications(context)
}

private fun issueHydrateReminder(context: Context) {
    NotificationUtils.remindUserBecauseItsTime(context)
}
