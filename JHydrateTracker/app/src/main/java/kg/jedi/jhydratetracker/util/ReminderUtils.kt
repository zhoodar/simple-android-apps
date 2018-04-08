package kg.jedi.jhydratetracker.util

import android.content.Context
import com.firebase.jobdispatcher.*
import kg.jedi.jhydratetracker.service.WaterReminderFirebaseJobService
import java.util.concurrent.TimeUnit

object ReminderUtils {

    private val REMINDER_JOB_TAG = "hydration_reminder_tag"
    private var sInitialized: Boolean = false

    @Synchronized
    fun scheduleChargingReminder(context: Context, reminderTime: Int) {
        if (sInitialized) return
        val driver = GooglePlayDriver(context)
        val dispatcher = FirebaseJobDispatcher(driver)

        val reminderIntervalSec = getSeconds(reminderTime)

        val constraintReminderJob = dispatcher.newJobBuilder()
                .setService(WaterReminderFirebaseJobService::class.java)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        reminderIntervalSec,
                        reminderIntervalSec + reminderIntervalSec))
                .setReplaceCurrent(true)
                .build()

        dispatcher.schedule(constraintReminderJob)

        sInitialized = true
    }

    private fun getSeconds(minutes: Int): Int {
        return TimeUnit.MINUTES.toSeconds(minutes.toLong()).toInt()
    }
}