package kg.jedi.jpomodoro.util;

import android.content.Context;

/**
 * @author Joodar on 3/21/18.
 */

public class JR {

    public static int getRInt(Context context, int i) {
        return context.getResources().getInteger(i);
    }

    public static String getRString(Context context, int i) {
        return context.getResources().getString(i);
    }
}
