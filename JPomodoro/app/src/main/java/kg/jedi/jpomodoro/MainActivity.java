package kg.jedi.jpomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import kg.jedi.jpomodoro.busines.PTState;
import kg.jedi.jpomodoro.busines.PomodoroTimer;
import kg.jedi.jpomodoro.busines.PomodoroTimerState;
import kg.jedi.jpomodoro.busines.SettingState;
import kg.jedi.jpomodoro.util.JR;
import kg.jedi.jpomodoro.util.Preference;

import static kg.jedi.jpomodoro.util.Preference.LONG_BREAK;
import static kg.jedi.jpomodoro.util.Preference.LONG_BREAK_COUNT;
import static kg.jedi.jpomodoro.util.Preference.POMODORO;
import static kg.jedi.jpomodoro.util.Preference.POMODORO_COUNT;
import static kg.jedi.jpomodoro.util.Preference.SHORT_BREAK;
import static kg.jedi.jpomodoro.util.Preference.SHORT_BREAK_COUNT;

public class MainActivity extends AppCompatActivity {

    private static final int oneSecInMilli = 1000;

    private SharedPreferences sharedPreferences;
    private PomodoroTimerState ptState;
    private SettingState settingState;
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
    private int mainTimeInSec;
    private String timerLabel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
        setCountPreference();
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

        ptState = new PomodoroTimerState(eventListener);
    }

    private void setCountPreference() {
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
        btnResetData.setOnClickListener(view -> alertDelete());
    }

    private void pauseTimer() {
        if (null != pmTimer) {
            stopTimer();
            ptState.changeState(PTState.ONPAUSE);
        }
    }

    private void startTimer() {
        if (!timerIsActive) {
            int milliSecs = mainTimeInSec * oneSecInMilli;
            pmTimer = new PomodoroTimer(milliSecs, oneSecInMilli, eventListener);
            pmTimer.start();
            timerIsActive = true;
            stopNotification();
        }
    }

    private void resetTimer() {
        if (null != pmTimer) {
            stopTimer();
            ptState.changeState(PTState.POMODORO);
            setCountPreference();
        }
    }

    private void stopTimer() {
        pmTimer.cancel();
        timerIsActive = false;
    }

    private void alertChanges() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(JR.getRString(getApplicationContext(), R.string.setting_change_message))
                .setNegativeButton(JR.getRString(getApplicationContext(), R.string.dialog_no),
                        (dialog, which) -> dialog.dismiss())
                .setPositiveButton(JR.getRString(getApplicationContext(), R.string.dialog_yes),
                        (dialog, which) -> {
                            dialog.dismiss();
                            settingState.changeState(SettingState.SState.OLD);
                            recreate();
                        });

        builder.show();
    }

    private void alertDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(JR.getRString(getApplicationContext(), R.string.delete_counts_message))
                .setNegativeButton(JR.getRString(getApplicationContext(), R.string.dialog_no),
                        (dialog, which) -> dialog.dismiss())
                .setPositiveButton(JR.getRString(getApplicationContext(), R.string.dialog_yes),
                        (dialog, which) -> {
                            dialog.dismiss();
                            deleteCount();
                        });

        builder.show();
    }

    private void deleteCount() {
        int defCount = JR.getRInt(getApplicationContext(), R.integer.def_count);
        editSharedPreferences(POMODORO_COUNT, defCount);
        editSharedPreferences(LONG_BREAK_COUNT, defCount);
        editSharedPreferences(SHORT_BREAK_COUNT, defCount);
        setCountPreference();
    }

    private void handleTimerStateChanges(PTState PTState) {
        int onMinInSec = 60;
        switch (PTState) {
            case ONPAUSE:
                mainTimeInSec = pmTimer.getLeftTime();
                break;
            case SHORT_BREAK:
                int shortBreakTime = getSettingFromSharedPreference(SHORT_BREAK, R.integer.def_pomodoro);
                mainTimeInSec = shortBreakTime * onMinInSec;
                timerLabel = JR.getRString(getApplicationContext(), R.string.label_short_break);
                break;
            case LONG_BREAK:
                int longBreakTime = getSettingFromSharedPreference(LONG_BREAK, R.integer.def_pomodoro);
                mainTimeInSec = longBreakTime * onMinInSec;
                timerLabel = JR.getRString(getApplicationContext(), R.string.label_long_break);
                break;
            default:
                int pomodoroTime = getSettingFromSharedPreference(POMODORO, R.integer.def_pomodoro);
                mainTimeInSec = pomodoroTime * onMinInSec;
                timerLabel = JR.getRString(getApplicationContext(), R.string.label_pomodoro);
        }
        tvTimerLabel.setText(timerLabel);
        updateUITimer(mainTimeInSec);
        setCountPreference();
    }

    private void handleTimerFinish() {
        int pomodoro = getCountFromSharedPreference(POMODORO_COUNT);
        int shortBreak = getCountFromSharedPreference(SHORT_BREAK_COUNT);
        int longBreak = getCountFromSharedPreference(LONG_BREAK_COUNT);
        stopTimer();

        if (PTState.SHORT_BREAK == ptState.getTimerState()) {
            editSharedPreferences(SHORT_BREAK_COUNT, shortBreak + 1);
            ptState.changeState(PTState.POMODORO);

        } else if (PTState.LONG_BREAK == ptState.getTimerState()) {
            editSharedPreferences(LONG_BREAK_COUNT, longBreak + 1);
            ptState.changeState(PTState.POMODORO);

        } else if (pomodoro != 0 && (pomodoro % 4) == 0
                && PTState.POMODORO == ptState.getTimerState()) {

            editSharedPreferences(POMODORO_COUNT, pomodoro + 1);
            ptState.changeState(PTState.LONG_BREAK);

        } else {
            editSharedPreferences(POMODORO_COUNT, pomodoro + 1);
            ptState.changeState(PTState.SHORT_BREAK);
        }
    }

    private void editSharedPreferences(String key, int value) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply();
    }

    private int getCountFromSharedPreference(String key) {
        return sharedPreferences.getInt(key, JR.getRInt(getApplicationContext(), R.integer.def_count));
    }

    private int getSettingFromSharedPreference(String key, int def) {
        return sharedPreferences.getInt(key, JR.getRInt(getApplicationContext(), def));
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
        stopNotification();
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
            Intent startSettingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startNotification() {
        startService(new Intent(MainActivity.this, MediaPlayerService.class));
    }

    private void stopNotification() {
        stopService(new Intent(MainActivity.this, MediaPlayerService.class));
    }

    private class EventListener
            implements PomodoroTimer.OnTimerChanged, PomodoroTimerState.OnTimerStateChanged {

        @Override
        public void onUpdate(int seconds) {
            updateUITimer(seconds);
        }

        @Override
        public void onFinish() {
            startNotification();
            handleTimerFinish();
        }

        @Override
        public void onStateChange(PTState state) {
            handleTimerStateChanges(state);
        }
    }

}