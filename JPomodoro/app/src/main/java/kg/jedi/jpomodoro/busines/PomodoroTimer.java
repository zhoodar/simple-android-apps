package kg.jedi.jpomodoro.busines;

import android.os.CountDownTimer;

/**
 * @author Joodar on 3/22/18.
 */

public class PomodoroTimer extends CountDownTimer {
    private int oneSecInMilli = 1000;
    private OnTimerChanged timerListener;

    private int secondsUntilFinished;

    public PomodoroTimer(long millisInFuture, long countDownInterval, OnTimerChanged listener) {
        super(millisInFuture, countDownInterval);
        timerListener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        secondsUntilFinished = (int) millisUntilFinished / oneSecInMilli;
        timerListener.onUpdate(secondsUntilFinished);
    }

    @Override
    public void onFinish() {
        timerListener.onFinish();
    }

    public int getLeftTime() {
        return secondsUntilFinished;
    }

    public interface OnTimerChanged {
        void onUpdate(int seconds);
        void onFinish();
    }
}
