package kg.jedi.jpomodoro.busines;

import java.io.Serializable;

/**
 * @author Joodar on 3/22/18.
 */

public class SettingState  implements Serializable {

    private SState state;

    private static SettingState INSTANCE = null;

    private SettingState() {
        state = SState.OLD;
    }

    public static SettingState getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new SettingState();
        }
        return INSTANCE;
    }

    public SState getState() {
        return this.state;
    }

    public void changeState(SState state) {
        this.state = state;
    }

    public enum SState {
        NEW, OLD
    }
}
