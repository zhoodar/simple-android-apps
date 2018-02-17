package kg.jedi.jpomodoro;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar timerSeekBar;
    private TextView timerTextView;
    private Button controllerButton;
    private Boolean counterIsActive = false;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
        setListeners();
    }

    private void init() {
        timerTextView = findViewById(R.id.timerTextView);
        controllerButton = findViewById(R.id.controllerButton);

        timerSeekBar = findViewById(R.id.timerSeekBar);
        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(30);
    }

    private void setListeners() {
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        controllerButton.setOnClickListener(v -> controlTimer());
    }

    private void controlTimer() {
        int oneSecInMilli = 1000;
        if (!counterIsActive) {
            counterIsActive = true;
            timerSeekBar.setEnabled(false);
            controllerButton.setText(R.string.btnStop);
            int milliSecs = timerSeekBar.getProgress() * oneSecInMilli + 100;

            countDownTimer = new CountDownTimer(milliSecs, oneSecInMilli) {

                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / oneSecInMilli);
                }

                @Override
                public void onFinish() {
                    resetTimer();
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(),R.raw.analog_watch_alarm);
                    mplayer.start();
                }
            }.start();

        } else {
            resetTimer();
        }
    }

    private void resetTimer() {
        timerTextView.setText(R.string.def_time);
        timerSeekBar.setProgress(30);
        countDownTimer.cancel();
        controllerButton.setText(R.string.btnStart);
        timerSeekBar.setEnabled(true);
        counterIsActive = false;
    }

    private void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String secondString = Integer.toString(seconds);

        if (seconds <= 9) {
            secondString = "0" + secondString;
        }

        timerTextView.setText(String.format("%d:%s", minutes, secondString));
    }
}