package kg.jedi.forecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import kg.jedi.forecast.adapter.ForecastAdapter;
import kg.jedi.forecast.data.ForecastPreferences;
import kg.jedi.forecast.utilities.NetworkUtils;
import kg.jedi.forecast.utilities.OpenWeatherJsonUtils;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<String[]> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FORECAST_LOADER_ID = 0;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private RecyclerView recyclerView;
    private ForecastAdapter mForecastAdapter;
    private EventListener eventListener;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        loadWeatherData();
    }

    private void init() {
        eventListener = new EventListener();
        mForecastAdapter = new ForecastAdapter(eventListener);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView = findViewById(R.id.recyclerview_forecast);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mForecastAdapter);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(eventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(eventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            invalidateData();
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, MainActivity.this);
            return true;
        }

        if (id == R.id.action_map) {
            openLocationInMap();
            return true;
        }

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadWeatherData() {
        LoaderCallbacks<String[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, bundleForLoader, callback);
    }

    private void invalidateData() {
        mForecastAdapter.setWeatherData(null);
    }

    private void showWeatherDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void openLocationInMap() {
        String addressString = ForecastPreferences.getPreferredWeatherLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
        }
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(MainActivity.this) {
            String[] mWeatherData = null;


            @Override
            protected void onStartLoading() {
                if (mWeatherData != null) {
                    deliverResult(mWeatherData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {

                String locationQuery = ForecastPreferences
                        .getDefaultWeatherLocation();

                URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);

                try {
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(weatherRequestUrl);

                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                            .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                    return simpleJsonWeatherData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(String[] data) {
                mWeatherData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String data[]) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mForecastAdapter.setWeatherData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showWeatherDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {
    }

    private class EventListener implements ForecastAdapter.ForecastAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onClick(String weatherForDay) {
            Context context = MainActivity.this;
            Class destinationClass = DetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay);

            startActivity(intentToStartDetailActivity);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            PREFERENCES_HAVE_BEEN_UPDATED = true;
        }
    }
}