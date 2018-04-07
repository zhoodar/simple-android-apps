package kg.jedi.jhydratetracker.util

import android.content.Context
import com.firebase.jobdispatcher.*
import kg.jedi.jhydratetracker.service.WaterReminderFirebaseJobService
import java.util.concurrent.TimeUnit

object ReminderUtils {

    private val REMINDER_INTERVAL_MINUTES = 1
    private val REMINDER_INTERVAL_SECONDS = TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES.toLong()).toInt()
    private val SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS

    private val REMINDER_JOB_TAG = "hydration_reminder_tag"

    private var sInitialized: Boolean = false

    @Synchronized
    fun scheduleChargingReminder(context: Context) {
        if (sInitialized) return
        val driver = GooglePlayDriver(context)
        val dispatcher = FirebaseJobDispatcher(driver)

        val constraintReminderJob = dispatcher.newJobBuilder()
                .setService(WaterReminderFirebaseJobService::class.java)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build()

        dispatcher.schedule(constraintReminderJob)

        sInitialized = true
    }
}