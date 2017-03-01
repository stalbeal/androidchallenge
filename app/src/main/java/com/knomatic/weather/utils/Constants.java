package com.knomatic.weather.utils;

/**
 * Created by stephany.berrio on 28/02/17.
 */

public class Constants {

    public static final String FORECAST_API_KEY = "dcfc2ef12fabe4e2c6cc1d608bac82a8";
    public static final double DEFAULT_LAT = 40.730610;
    public static final double DEFAULT_LONG = -73.935242;
    public static final String COMPLETE_DATE_FORMAT = "EEE, MMM d";
    public static final String DAY_NAME_FORMAT = "EEE";
    public static final String FORECAST_DATA = "forecastData";
    public static final String LATITUDE_DATA = "latData";
    public static final String LONGITUDE_DATA = "lngData";
    public static final String ANIMATION_INDICATOR = "PacmanIndicator";
    public static final String GENERIC_ERROR = "Oops! Something went wrong!";
    public static final String NO_INTERNET_ERROR = "Please, check your connection";

    /* Request codes */
    public static final int REQUEST_PERMISSION_CODE = 1002;
    public static final int REQUEST_CONFIG_LOCATION_CODE = 1003;

    /* Bundle */
    public static final String CURRENTLY_WEATHER = "currently_weather";
    public static final String DAILY_WEATHER = "daily_weather";
    public static final String TIMEZONE = "timezone";
    public static final String APP_COLOR = "appColor";

    /* Rational permission dialog */
    public static final String DIALOG_TITLE = "¡Hi!";
    public static final String DIALOG_MESSAGE = "We need access to your location";
    public static final String DIALOG_ACCEPT = "Ok";

}
