package com.knomatic.weather.applications;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;

import com.knomatic.weather.utils.Constants;
import com.squareup.otto.Bus;


/**
 * Created by stephany.berrio on 28/02/17.
 */

public class WeatherApplication extends MultiDexApplication{

    private Bus bus;
    public static WeatherApplication instance;

    public static WeatherApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;

        /* Bus initialization*/
        bus = new Bus();

        /* Forecast initialization */
        ForecastConfiguration configuration =
                new ForecastConfiguration.Builder(Constants.FORECAST_API_KEY)
                        .setCacheDirectory(getCacheDir())
                        .build();
        ForecastClient.create(configuration);
    }

    public Bus getBus() {
        return bus;
    }
}
