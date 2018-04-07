package kg.jedi.jhydratetracker.service

import android.app.IntentService
import android.content.Intent

import kg.jedi.jhydratetracker.util.ReminderTasks

class WaterReminderIntentService : IntentService("WaterReminderIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val action = intent!!.action
        ReminderTasks.executeTask(this, action!!)
    }
}