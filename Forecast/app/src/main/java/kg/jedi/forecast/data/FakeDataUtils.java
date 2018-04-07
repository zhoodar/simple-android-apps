package kg.jedi.forecast.data;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kg.jedi.forecast.data.WeatherContract.WeatherEntry;
import kg.jedi.forecast.utilities.ForecastDateUtils;

/**
 * @author Joodar on 3/25/18.
 */

public class FakeDataUtils {

    private static int[] weatherIDs = {200, 300, 500, 711, 900, 962};

    private static ContentValues createTestWeatherContentValues(long date) {
        ContentValues testWeatherValues = new ContentValues();
        testWeatherValues.put(WeatherEntry.COLUMN_DATE, date);
        testWeatherValues.put(WeatherEntry.COLUMN_DEGREES, Math.random() * 2);
        testWeatherValues.put(WeatherEntry.COLUMN_HUMIDITY, Math.random() * 100);
        testWeatherValues.put(WeatherEntry.COLUMN_PRESSURE, 870 + Math.random() * 100);
        int maxTemp = (int) (Math.random() * 100);
        testWeatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, maxTemp);
        testWeatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, maxTemp - (int) (Math.random() * 10));
        testWeatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, Math.random() * 10);
        testWeatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, weatherIDs[(int) (Math.random() * 10) % 5]);
        return testWeatherValues;
    }


    public static void insertFakeData(Context context) {
        long today = ForecastDateUtils.normalizeDate(System.currentTimeMillis());
        List<ContentValues> fakeValues = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            fakeValues.add(FakeDataUtils.createTestWeatherContentValues(today + TimeUnit.DAYS.toMillis(i)));
        }
        context.getContentResolver().bulkInsert(
                WeatherEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[7]));
    }
}
