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

    public void changeState(PTState state) {
        this.timerPTState = state;
        stateListener.onStateChange(state);
    }

    public PTState getTimerState() {
        return this.timerPTState;
    }

    public interface OnTimerStateChanged {
        void onStateChange(PTState state);
    }
}
