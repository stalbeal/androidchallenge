package com.knomatic.weather.providers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.knomatic.weather.applications.WeatherApplication;

import static android.content.Context.LOCATION_SERVICE;
import static com.google.ads.AdRequest.LOGTAG;
import static com.knomatic.weather.utils.Constants.REQUEST_CONFIG_LOCATION_CODE;

/**
 * Created by stephany.berrio on 28/02/17.
 */

public class LocationProvider implements LocationListener {

    private static final LocationProvider ourInstance = new LocationProvider();

    private LocationRequest locationRequest;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private Location location;
    private boolean canGetLocation = false;
    /* The minimum distance to change Updates in meters */
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    /* The minimum time between updates in milliseconds */
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;

    public static LocationProvider getInstance() {
        return ourInstance;
    }

    private LocationProvider() {
    }

    /**
     * Method for get location updates
     *
     * @param apiClient
     * @param activity
     */
    public void enableLocationUpdates(GoogleApiClient apiClient, final Activity activity) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        /* GPS active */
                        getLocation(activity);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        /* GPS not active */
                        try {
                            status.startResolutionForResult(activity, REQUEST_CONFIG_LOCATION_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(LOGTAG, "GPS Error");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "GPS Error");
                        break;
                }
            }
        });
    }

    /**
     * Method to get the location
     *
     * @param context
     */
    public void getLocation(Context context) {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                /* no network provider is enabled */
            } else {
                this.canGetLocation = true;
                /* Get location from Network Provider */
                if (isNetworkEnabled) {
                    if (checkPermission(context)) {

                        PermissionProvider.getInstance().askForPermissions((AppCompatActivity) context,
                                Manifest.permission.ACCESS_FINE_LOCATION);
                        return;
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, LocationProvider.this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                /* Event with resulting location */
                                WeatherApplication.getInstance().getBus().post(location);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                locationManager.removeUpdates(this);
                            }
                        }
                    }
                }
                /* if GPS Enabled get lat/long using GPS Services */
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                /* Event with existing location */
                                WeatherApplication.getInstance().getBus().post(location);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                locationManager.removeUpdates(this);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        /* Event with location */
        WeatherApplication.getInstance().getBus().post(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean checkPermission(Context context) {
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }


}
