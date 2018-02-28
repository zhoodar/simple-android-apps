package kg.jedi.forecast.data;

import android.content.Context;

public class ForecastPreferences {

    private static final String DEFAULT_WEATHER_LOCATION_BISHKEK_ID = "1528334";

    public static String getPreferredWeatherLocation(Context context) {
        return getDefaultWeatherLocation();
    }

    public static boolean isMetric(Context context) {
        return true;
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION_BISHKEK_ID;
    }

}