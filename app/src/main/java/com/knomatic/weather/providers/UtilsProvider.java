package com.knomatic.weather.providers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.knomatic.weather.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.knomatic.weather.R;

/**
 * Created by stephany.berrio on 28/02/17.
 */

public class UtilsProvider {
    private static UtilsProvider instance = null;
    private String errorMessage;

    private UtilsProvider() {
    }

    public static UtilsProvider getInstance() {
        if (instance == null) {
            instance = new UtilsProvider();
        }
        return instance;
    }

    /**
     * Method to return the drawable resource
     *
     * @param reference name that makes reference to a weather state
     * @return
     */
    public int getWeatherIcon(String reference) {
        switch (reference) {
            case "clear-day":
                return R.drawable.ic_sun;
            case "clear-night":
                return R.drawable.ic_moon;
            case "rain":
                return R.drawable.ic_cloud_rain;
            case "snow":
                return R.drawable.ic_snowflake;
            case "sleet":
                return R.drawable.ic_cloud_snow_alt;
            case "wind":
                return R.drawable.ic_wind;
            case "fog":
                return R.drawable.ic_cloud_fog_alt;
            case "cloudy":
                return R.drawable.ic_cloud;
            case "partly-cloudy-day":
                return R.drawable.ic_cloud_sun;
            case "partly-cloudy-night":
                return R.drawable.ic_cloud_moon;
            case "hail":
                return R.drawable.ic_cloud_hail;
            case "thunderstorm":
                return R.drawable.ic_cloud_lightning;
            case "tornado":
                return R.drawable.ic_tornado;
            default:
                return R.drawable.ic_cloud;
        }
    }

    /**
     * Method to get string with the date
     *
     * @param date   date to transform
     * @param format format of return
     * @return
     */
    public String getDateText(Date date, String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
    }

    /**
     * Method to change fahrenheit temperature to celcius
     *
     * @param temperature
     * @return
     */
    public String getCelsiusTemperature(double temperature) {
        BigDecimal aux = new BigDecimal((temperature - 32) / 1.8);
        return String.valueOf(aux.intValue()) + "˚";
    }

    /**
     * Method to get one random color
     *
     * @return
     */
    public int getRandomColor() {
        int[] colorsArray = {R.color.red, R.color.pink, R.color.purple, R.color.blue,
                R.color.brown, R.color.green, R.color.deep_purple, R.color.indigo,
                R.color.dark_light_blue, R.color.medium_cyan, R.color.teal, R.color.deep_orange};
        int idx = new Random().nextInt(colorsArray.length);
        return colorsArray[idx];
    }

    /**
     * Method for check if exists internet connection
     *
     * @param context
     * @return
     */
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Method to get message when ocurrs an error
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Method to set message when ocurrs an error
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
