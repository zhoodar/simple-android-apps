package kg.jedi.forecast.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import kg.jedi.forecast.R;

public class ForecastPreferences {

    private static final String DEFAULT_WEATHER_LOCATION_BISHKEK_ID = "1528334";


    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_metric);
        String preferredUnits = prefs.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_units_metric);
        return metric.equals(preferredUnits);
    }

    public static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION_BISHKEK_ID;
    }

    public static String getPreferredWeatherLocation(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);
        return prefs.getString(keyForLocation, defaultLocation);
    }

}