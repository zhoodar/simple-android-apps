package kg.jedi.jpomodoro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import kg.jedi.jpomodoro.busines.SettingState;
import kg.jedi.jpomodoro.util.Preference;

import static kg.jedi.jpomodoro.util.Preference.LONG_BREAK;
import static kg.jedi.jpomodoro.util.Preference.POMODORO;
import static kg.jedi.jpomodoro.util.Preference.SHORT_BREAK;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SeekBar sbPomodoro;
    private SeekBar sbShortBreak;
    private SeekBar sbLongBreak;
    private TextView tvPomodoro;
    private TextView tvLongBreak;
    private TextView tvShortBreak;
    private SettingState settingState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        init();
        setData();
        setListeners();
    }

    private void init() {
        settingState = SettingState.getInstance();
        sharedPreferences = getSharedPreferences(Preference.PREF_NAME, MODE_PRIVATE);
        sbPomodoro = findViewById(R.id.sbPomodoro);
        sbShortBreak = findViewById(R.id.sbShortBreak);
        sbLongBreak = findViewById(R.id.sbLongBreak);

        tvPomodoro = findViewById(R.id.tvPomodoro);
        tvLongBreak = findViewById(R.id.tvLongBreak);
        tvShortBreak = findViewById(R.id.tvShortBreak);
    }

    private void setData() {
        int pomodoro = sharedPreferences.getInt(POMODORO, getRInt(R.integer.def_pomodoro));
        int longBreak = sharedPreferences.getInt(LONG_BREAK, getRInt(R.integer.def_long_break));
        int shortBreak = sharedPreferences.getInt(SHORT_BREAK, getRInt(R.integer.def_short_break));

        sbPomodoro.setProgress(pomodoro);
        sbPomodoro.setMax(getRInt(R.integer.max_pomodoro));
        tvPomodoro.setText(String.valueOf(pomodoro));

        sbLongBreak.setProgress(longBreak);
        sbLongBreak.setMax(getRInt(R.integer.max_long_break));
        tvLongBreak.setText(String.valueOf(longBreak));

        sbShortBreak.setProgress(shortBreak);
        sbShortBreak.setMax(getRInt(R.integer.max_short_break));
        tvShortBreak.setText(String.valueOf(shortBreak));
    }

    private void setListeners() {

        sbPomodoro.setOnSeekBarChangeListener(new SeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPomodoro.setText(String.valueOf(progress));
                savePreference(POMODORO, progress);
            }
        });

        sbShortBreak.setOnSeekBarChangeListener(new SeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvShortBreak.setText(String.valueOf(progress));
                savePreference(SHORT_BREAK, progress);
            }
        });

        sbLongBreak.setOnSeekBarChangeListener(new SeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLongBreak.setText(String.valueOf(progress));
                savePreference(LONG_BREAK, progress);
            }
        });

    }

    private void savePreference(String key, int value) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply();
        settingState.changeState(SettingState.SState.NEW);
    }

    private int getRInt(int i) {
        return getResources().getInteger(i);
    }

    private abstract class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
