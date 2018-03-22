package kg.jedi.jpomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import kg.jedi.jpomodoro.busines.PTState;
import kg.jedi.jpomodoro.busines.PomodoroTimer;
import kg.jedi.jpomodoro.busines.PomodoroTimerState;
import kg.jedi.jpomodoro.busines.SettingState;
import kg.jedi.jpomodoro.util.Preference;
import kg.jedi.jpomodoro.util.Resource;

import static kg.jedi.jpomodoro.util.Preference.LONG_BREAK;
import static kg.jedi.jpomodoro.util.Preference.POMODORO;
import static kg.jedi.jpomodoro.util.Preference.SHORT_BREAK;

public class MainActivity extends AppCompatActivity {

    private static final int oneSecInMilli = 1000;
    private static final String POMODORO_COUNT = "pomodoroCount";
    private static final String SHORT_BREAK_COUNT = "shortBreakCount";
    private static final String LONG_BREAK_COUNT = "longBreakCount";

    private SharedPreferences sharedPreferences;
    private MediaPlayer mPlayer;
    private PomodoroTimerState ptState;
    private Boolean timerIsActive = false;
    private PomodoroTimer pmTimer;
    private EventListener eventListener;

    private TextView tvTimer;
    private TextView tvTimerLabel;
    private TextView tvPomodoroCount;
    private TextView tvShortBreakCount;
    private TextView tvLongBreakCount;

    private Button btnStart;
    private Button btnReset;
    private Button btnPause;
    private Button btnResetData;

    private int pomodoroTime;
    private int shortBreakTime;
    private int longBreakTime;
    private String timerLabel = "";
    private int mainTimeInSec;
    private SettingState settingState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
        setPreferenceData();
        setListeners();

        ptState.changeState(PTState.POMODORO);//def state
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (settingState.getState() == SettingState.SState.NEW) {
            alertChanges();
        }
    }

    private void init() {
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.analog_watch_alarm);
        sharedPreferences = getSharedPreferences(Preference.PREF_NAME, MODE_PRIVATE);
        eventListener = new EventListener();
        settingState = SettingState.getInstance();

        tvTimer = findViewById(R.id.tvTimer);
        tvTimerLabel = findViewById(R.id.tvTimerLabel);
        tvPomodoroCount = findViewById(R.id.tvPomodoroCount);
        tvShortBreakCount = findViewById(R.id.tvShortBreakCount);
        tvLongBreakCount = findViewById(R.id.tvLongBreakCount);

        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        btnPause = findViewById(R.id.btnPause);
        btnResetData = findViewById(R.id.btnResetData);

        pomodoroTime = sharedPreferences.getInt(POMODORO,
                Resource.getRInt(getApplicationContext(), R.integer.def_pomodoro));
        shortBreakTime = sharedPreferences.getInt(SHORT_BREAK,
                Resource.getRInt(getApplicationContext(), R.integer.def_short_break));
        longBreakTime = sharedPreferences.getInt(LONG_BREAK,
                Resource.getRInt(getApplicationContext(), R.integer.def_long_break));
        ptState = new PomodoroTimerState(eventListener);
    }

    private void setPreferenceData() {
        tvTimer.setText(String.format(Locale.getDefault(), "%d:%s", pomodoroTime, "00"));

        int pomodoro = getCountFromSharedPreference(POMODORO_COUNT);
        int shortBreak = getCountFromSharedPreference(SHORT_BREAK_COUNT);
        int longBreak = getCountFromSharedPreference(LONG_BREAK_COUNT);
        tvPomodoroCount.setText(String.valueOf(pomodoro));
        tvShortBreakCount.setText(String.valueOf(shortBreak));
        tvLongBreakCount.setText(String.valueOf(longBreak));
    }

    private void setListeners() {
        btnStart.setOnClickListener(view -> startTimer());

        btnReset.setOnClickListener(view -> resetTimer());

        btnPause.setOnClickListener(view -> pauseTimer());

        btnResetData.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Not yet!", Toast.LENGTH_SHORT).show();
        });
    }

    private void pauseTimer() {
        pmTimer.cancel();
        timerIsActive = false;
        ptState.changeState(PTState.ONPAUSE);
    }

    private void startTimer() {
        if (!timerIsActive) {
            mPlayer.stop();
            int milliSecs = mainTimeInSec * oneSecInMilli + 100;
            pmTimer = new PomodoroTimer(milliSecs, oneSecInMilli, eventListener);
            pmTimer.start();
            handleTimerStateChanges(ptState.getTimerState());
            timerIsActive = true;
        }
    }

    private void resetTimer() {
        timerIsActive = false;
        pmTimer.cancel();
        ptState.changeState(PTState.POMODORO);
        setPreferenceData();
    }

    private void alertChanges() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to reload changed settings?")
                .setNegativeButton("No", (dialog, which) ->  dialog.dismiss())
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    settingState.changeState(SettingState.SState.OLD);
                    recreate();
                });

        builder.show();
    }

    private void handleTimerStateChanges(PTState PTState) {
        int onMinInSec = 60;
        switch (PTState) {
            case ONPAUSE:
                mainTimeInSec = pmTimer.getLeftTime();
                break;
            case SHORT_BREAK:
                mainTimeInSec = shortBreakTime * onMinInSec;
                timerLabel = Resource.getRString(getApplicationContext(), R.string.label_short_break);
                break;
            case LONG_BREAK:
                mainTimeInSec = longBreakTime * onMinInSec;
                timerLabel = Resource.getRString(getApplicationContext(), R.string.label_long_break);
                break;
            default:
                mainTimeInSec = pomodoroTime * onMinInSec;
                timerLabel = Resource.getRString(getApplicationContext(), R.string.label_pomodoro);
        }
        tvTimerLabel.setText(timerLabel);
        updateUITimer(mainTimeInSec);
    }

    private void handleTimerFinish() {
        int pomodoro = getCountFromSharedPreference(POMODORO_COUNT);
        int shortBreak = getCountFromSharedPreference(SHORT_BREAK_COUNT);
        int longBreak = getCountFromSharedPreference(LONG_BREAK_COUNT);
        pmTimer.cancel();
        timerIsActive = false;

        if (PTState.SHORT_BREAK == ptState.getTimerState() || PTState.LONG_BREAK == ptState.getTimerState()) {
            ptState.changeState(PTState.POMODORO);
            sharedPreferences.edit()
                    .putInt(POMODORO_COUNT, pomodoro + 1)
                    .apply();

        } else if ((pomodoro % 4) == 0 && PTState.POMODORO == ptState.getTimerState()) {
            ptState.changeState(PTState.LONG_BREAK);
            sharedPreferences.edit()
                    .putInt(LONG_BREAK_COUNT, longBreak + 1)
                    .apply();

        } else {
            ptState.changeState(PTState.SHORT_BREAK);
            sharedPreferences.edit()
                    .putInt(SHORT_BREAK_COUNT, shortBreak + 1)
                    .apply();
        }

    }

    private int getCountFromSharedPreference(String key) {
        return sharedPreferences.getInt(key,
                Resource.getRInt(getApplicationContext(), R.integer.def_count));
    }

    private void updateUITimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String secondString = Integer.toString(seconds);
        if (seconds <= 9) {
            secondString = "0" + secondString;
        }

        tvTimer.setText(String.format(Locale.getDefault(), "%d:%s", minutes, secondString));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mPlayer.stop();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            settingState.changeState(SettingState.SState.OLD);
            startSettingsActivity.putExtra("state", settingState);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class EventListener
            implements PomodoroTimer.OnTimerChanged, PomodoroTimerState.OnTimerStateChanged {

        @Override
        public void onUpdate(int seconds) {
            updateUITimer(seconds);
        }

        @Override
        public void onFinish() {
            mPlayer.start();
            handleTimerFinish();
        }

        @Override
        public void onStateChange(PTState PTState) {
            handleTimerStateChanges(PTState);
        }
    }

}