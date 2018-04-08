package kg.jedi.jhydratetracker.util

import android.content.Context
import android.preference.PreferenceManager

object PreferenceUtils {

    private val KEY_WATER_COUNT = "water-count"
    private val KEY_REMINDER_TIME = "reminder-time"
    private val DEFAULT_COUNT = 0
    private val DEFAULT_REMINDER_TIME = 45

    private fun setWaterCount(context: Context, glassesOfWater: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY_WATER_COUNT, glassesOfWater)
        editor.apply()
    }

    fun getWaterCount(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT)
    }

    @Synchronized
    fun incrementWaterCount(context: Context) {
        var waterCount = PreferenceUtils.getWaterCount(context)
        setWaterCount(context, ++waterCount)
    }

    fun setReminderTime(context: Context, value: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        prefs
                .edit()
                .putInt(KEY_REMINDER_TIME, value)
                .apply()
    }

    fun getReminderTime(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY_REMINDER_TIME, DEFAULT_REMINDER_TIME)
    }
}