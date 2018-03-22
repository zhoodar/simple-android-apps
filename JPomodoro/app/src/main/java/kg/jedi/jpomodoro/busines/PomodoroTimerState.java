package kg.jedi.jpomodoro.busines;

/**
 * @author Joodar on 3/22/18.
 */

public class PomodoroTimerState {

    private OnTimerStateChanged stateListener;

    private PTState timerPTState;

    public PomodoroTimerState(OnTimerStateChanged listener) {
        this.stateListener = listener;
    }

    public void changeState(PTState PTState) {
        this.timerPTState = PTState;
        stateListener.onStateChange(PTState);
    }

    public PTState getTimerState() {
        return this.timerPTState;
    }

    public interface OnTimerStateChanged {
        void onStateChange(PTState PTState);
    }
}
