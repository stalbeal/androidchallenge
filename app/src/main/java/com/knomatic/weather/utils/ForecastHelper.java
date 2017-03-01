package com.knomatic.weather.utils;

import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.models.Forecast;

import com.knomatic.weather.applications.WeatherApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by stephany.berrio on 28/02/17.
 */

public class ForecastHelper {
    private static ForecastHelper instance = null;


    private ForecastHelper() {
    }

    public static ForecastHelper getInstance() {

        if (instance == null) {
            instance = new ForecastHelper();
        }
        return instance;

    }

    /**
     * Method to get the forecast from service
     * @param latitude
     * @param longitude
     */
    public void getForecast(double latitude, double longitude) {

        ForecastClient.getInstance().getForecast(latitude, longitude, new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {
                if (response.isSuccessful()) {
                    WeatherApplication.getInstance().getBus().post(response.body());
                } else {
                    //iForecastCallback.onForecastError();
                }
            }

            @Override
            public void onFailure(Call<Forecast> forecastCall, Throwable t) {
                // iForecastCallback.onForecastError();
            }
        });
    }


}
