package kg.jedi.jhydratetracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kg.jedi.jhydratetracker.util.PreferenceUtils
import kotlinx.android.synthetic.main.settings_activity.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val reminderTime = PreferenceUtils.getReminderTime(applicationContext)
        discreteSeekBar.progress = reminderTime
        handleProgressChange(reminderTime)

        setListeners()
    }

    private fun setListeners() {
        discreteSeekBar.setOnProgressChangeListener(object : OnProgressChange() {

            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
                handleProgressChange(value)
            }
        })
    }

    private fun handleProgressChange(value: Int) {
        val hour = value / 60
        val min = value - (hour * 60)

        var sMin = min.toString()
        if (min < 9) {
            sMin = "0$min"
        }

        val hm = "$hour:$sMin"
        tvReminder.text = hm
        PreferenceUtils.setReminderTime(applicationContext, value)
    }

    abstract class OnProgressChange : DiscreteSeekBar.OnProgressChangeListener {

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
        }
    }
}


