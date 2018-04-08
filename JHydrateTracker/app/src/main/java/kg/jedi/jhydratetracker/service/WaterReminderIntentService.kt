package kg.jedi.jhydratetracker.service

import android.app.IntentService
import android.content.Intent
import kg.jedi.jhydratetracker.util.executeTask

class WaterReminderIntentService : IntentService("WaterReminderIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val action = intent!!.action
        executeTask(this, action!!)
    }
}