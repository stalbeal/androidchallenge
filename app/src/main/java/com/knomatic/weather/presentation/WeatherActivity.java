package com.knomatic.weather.presentation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.zetterstrom.com.forecast.models.Forecast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.knomatic.weather.R;
import com.knomatic.weather.applications.WeatherApplication;
import com.knomatic.weather.models.events.ErrorEvent;
import com.knomatic.weather.presentation.fragments.CurrentWeatherFragment;
import com.knomatic.weather.presentation.fragments.WeekWeatherFragment;
import com.knomatic.weather.providers.DialogProvider;
import com.knomatic.weather.providers.LocationProvider;
import com.knomatic.weather.providers.PermissionProvider;
import com.knomatic.weather.providers.UtilsProvider;
import com.knomatic.weather.utils.Constants;
import com.knomatic.weather.utils.ForecastHelper;
import com.squareup.otto.Subscribe;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by stephany.berrio on 28/02/17.
 */

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private GoogleApiClient apiClient;
    private Location defaultLocation = new Location("");
    private SharedPreferences prefs = null;
    private Forecast savedForecast = null;

    /* Views */
    private Toolbar appbar;
    private int appColor = R.color.indigo;
    private AVLoadingIndicatorView avi;
    private LinearLayout lyLoadingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initVars();
        initViewComponents();
    }

    /**
     * Method to init vars
     */
    private void initVars() {
        String json;
        prefs = getPreferences(Context.MODE_PRIVATE);
        WeatherApplication.getInstance().getBus().register(this);

        appColor = UtilsProvider.getInstance().getRandomColor();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        if (prefs.getString(Constants.LATITUDE_DATA, null) != null &&
                prefs.getString(Constants.LATITUDE_DATA, null) != null) {

            defaultLocation = new Location("");
            defaultLocation.setLatitude(Double.parseDouble(prefs.getString(Constants.LATITUDE_DATA, null)));
            defaultLocation.setLongitude(Double.parseDouble(prefs.getString(Constants.LATITUDE_DATA, null)));
        } else {
            defaultLocation.setLatitude(Constants.DEFAULT_LAT);
            defaultLocation.setLongitude(Constants.DEFAULT_LONG);
        }

        if (prefs.getString(Constants.FORECAST_DATA, null) != null) {
            json = prefs.getString(Constants.FORECAST_DATA, "");
            Gson gson = new Gson();
            savedForecast = gson.fromJson(json, Forecast.class);
        }
    }

    /**
     * Method to init the view components and build the
     * view for the activity
     */
    private void initViewComponents() {
        lyLoadingContainer = (LinearLayout) findViewById(R.id.ly_loading_container);
        appbar = (Toolbar) findViewById(R.id.appbar);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        populateViews();
        startAnim();

    }

    /**
     * Method to set properties for views
     */
    private void populateViews() {
        /* Toolbar properties */
        appbar.setBackground(new ColorDrawable(getResources().getColor(appColor)));
        setSupportActionBar(appbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_filter_drama_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Animation */
        lyLoadingContainer.setBackground(new ColorDrawable(getResources().getColor(appColor)));
        String indicator = getIntent().getStringExtra(Constants.ANIMATION_INDICATOR);
        avi.setIndicator(indicator);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        /* Check internet connection*/
        if (UtilsProvider.getInstance().isOnline(this)) {
            /* Check permissions */
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                PermissionProvider.getInstance().setRationalMessage(Constants.DIALOG_MESSAGE);
                PermissionProvider.getInstance().setRationalTitle(Constants.DIALOG_TITLE);
                PermissionProvider.getInstance().setRationalTxtBtn(Constants.DIALOG_ACCEPT);
                PermissionProvider.getInstance().askForPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                LocationProvider.getInstance().enableLocationUpdates(apiClient, this);
            }
        } else {
            showAlertDialog();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        /**
         * If the connection to Play services is suspended, we get the
         * forecast with the default location
         **/
        getForecast(defaultLocation);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        /**
         * If the connection to Play services fail, we get the
         * forecast with the default location
         **/
        getForecast(defaultLocation);
    }


    private void getForecast(Location location) {
        ForecastHelper.getInstance().getForecast(location.getLatitude(), location.getLongitude());

    }

    /**
     * Method that is listening for when the forecast is obtained
     *
     * @param forecast obj obtained
     */
    @Subscribe
    public void onForecastResponse(Forecast forecast) {

        saveInPreferences(forecast, Constants.FORECAST_DATA);

        /* Set fragments */
        setFragment(R.id.fl_current_status,
                CurrentWeatherFragment.newInstance(forecast.getCurrently(), forecast.getTimezone(), appColor));
        setFragment(R.id.fl_week_status, WeekWeatherFragment.newInstance(forecast.getDaily()));

        stopAnim();
    }

    /**
     * Method to display an alert dialog with a message
     * @param errorEvent
     */
    @Subscribe
    public void onError(ErrorEvent errorEvent){
        UtilsProvider.getInstance().setErrorMessage(errorEvent.getError());
        DialogFragment newFragment = new DialogProvider();
        newFragment.show(getSupportFragmentManager(), Constants.GENERIC_ERROR);
    }

    /**
     * Method for save objects in shared preferences
     *
     * @param obj object to save
     * @param key name for relate the object
     **/
    private void saveInPreferences(Object obj, String key) {
        /* Save forecast obj in preferences */
        SharedPreferences.Editor prefsEditor = getPreferences(Context.MODE_PRIVATE).edit();
        String json = new Gson().toJson(obj);
        prefsEditor.putString(key, json);
        prefsEditor.apply();

    }

    /**
     * Method that is listening for when the location is obtained
     *
     * @param location
     */
    @Subscribe
    public void onLocationRequestSuccess(Location location) {
        getPreferences(MODE_PRIVATE).edit().putString(Constants.LATITUDE_DATA,
                Double.toString(location.getLatitude())).apply();
        getPreferences(MODE_PRIVATE).edit().putString(Constants.LONGITUDE_DATA,
                Double.toString(location.getLongitude())).apply();
        getForecast(location);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /***
                 * Como nos dieron permisos ahora si vamos a pedir la ubicacion
                 */
                LocationProvider.getInstance().enableLocationUpdates(apiClient, this);
            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                getForecast(defaultLocation);
                Log.e("Permiso", "Permiso denegado");
            }
        }
    }

    /**
     * Method for set fragments
     *
     * @param fragment
     */
    public void setFragment(int frameId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment)
                .commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CONFIG_LOCATION_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        LocationProvider.getInstance().getLocation(this);
                        break;
                    case Activity.RESULT_CANCELED:
                        getForecast(defaultLocation);
                        Log.i("", "El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
        }
    }

    /**
     * Method to display alert dialog when there is no internet connection
     */
    public void showAlertDialog() {
        UtilsProvider.getInstance().setErrorMessage(Constants.NO_INTERNET_ERROR);
        DialogFragment newFragment = new DialogProvider();
        newFragment.show(getSupportFragmentManager(), "no_internet");

        if (savedForecast != null) {
            setFragment(R.id.fl_current_status,
                    CurrentWeatherFragment.newInstance(savedForecast.getCurrently(), savedForecast.getTimezone(), appColor));
            setFragment(R.id.fl_week_status, WeekWeatherFragment.newInstance(savedForecast.getDaily()));
            stopAnim();
        }

    }

    /**
     * Method for start animation
     */
    void startAnim() {
        avi.setVisibility(View.VISIBLE);
        lyLoadingContainer.setVisibility(View.VISIBLE);
        avi.post(new Runnable() {
            @Override
            public void run() {
                avi.show();
            }
        });

    }

    /**
     * Method for stop animation
     */
    void stopAnim() {
        avi.post(new Runnable() {
            @Override
            public void run() {
                avi.hide();
            }
        });
        avi.setVisibility(View.GONE);
        lyLoadingContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeatherApplication.getInstance().getBus().unregister(this);

    }

}