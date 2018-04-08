package kg.jedi.jhydratetracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kg.jedi.jhydratetracker.Actions.ACTION_INCREMENT_WATER_COUNT
import kg.jedi.jhydratetracker.service.WaterReminderIntentService
import kg.jedi.jhydratetracker.util.PreferenceUtils
import kg.jedi.jhydratetracker.util.ReminderUtils
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        updateWaterCount()
        val reminderTime = PreferenceUtils.getReminderTime(applicationContext)

        ReminderUtils.scheduleChargingReminder(this, reminderTime)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    private fun updateWaterCount() {
        val waterCount = PreferenceUtils.getWaterCount(this)
        tvWaterCount.text = waterCount.toString()
    }

    fun incrementWater(view: View) {
        if (mToast != null) mToast!!.cancel()
        mToast = Toast.makeText(this, R.string.water_chug_toast, Toast.LENGTH_SHORT)
        mToast!!.show()

        val incrementWaterCountIntent = Intent(applicationContext, WaterReminderIntentService::class.java)
        incrementWaterCountIntent.action = ACTION_INCREMENT_WATER_COUNT
        startService(incrementWaterCountIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId

        when (itemId) {
            R.id.settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        updateWaterCount()
    }
}
