package com.knomatic.weather.presentation.fragments;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.zetterstrom.com.forecast.models.DataPoint;

import com.knomatic.weather.R;
import com.knomatic.weather.providers.UtilsProvider;
import com.knomatic.weather.utils.Constants;

/**
 * Fragment to display the current weather
 */
public class CurrentWeatherFragment extends Fragment {


    private DataPoint currentlyWeather;
    private String timezone;
    private int appColor = R.color.indigo;

    /* Views */
    private TextView tvTemperature;
    private TextView tvDate;
    private TextView tvTimezone;
    private TextView tvSummary;
    private ImageView ivTemperatureIcon;
    private ImageView ivBackgroundIcon;
    private LinearLayout lyCurrentStateContainer;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CurrentStatusFragment.
     * @param currently object with currently weather info
     * @param timezone  timezone info to display in the view
     */
    public static CurrentWeatherFragment newInstance(DataPoint currently, String timezone, int appColor) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.CURRENTLY_WEATHER, currently);
        args.putString(Constants.TIMEZONE, timezone);
        args.putInt(Constants.APP_COLOR, appColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentlyWeather = (DataPoint) getArguments().getSerializable(Constants.CURRENTLY_WEATHER);
        timezone = getArguments().getString(Constants.TIMEZONE);
        appColor = getArguments().getInt(Constants.APP_COLOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        initViewComponents(view);
        return view;
    }

    /**
     * Method to init the view components and build the
     * view for the activity
     */
    private void initViewComponents(View view) {
        ivBackgroundIcon = (ImageView) view.findViewById(R.id.iv_background_view);
        ivTemperatureIcon = (ImageView) view.findViewById(R.id.iv_temperature_icon);
        tvTemperature = (TextView) view.findViewById(R.id.tv_temperature);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvTimezone = (TextView) view.findViewById(R.id.tv_timezone);
        tvSummary = (TextView) view.findViewById(R.id.tv_sumary);
        lyCurrentStateContainer = (LinearLayout) view.findViewById(R.id.ly_container_current_status);

        populateViews();

    }

    /**
     * Method to set properties for views
     */
    private void populateViews() {
        lyCurrentStateContainer.setBackground(new ColorDrawable(getResources().getColor(appColor)));

        ivBackgroundIcon.setImageResource(UtilsProvider.getInstance()
                .getWeatherIcon(currentlyWeather.getIcon().getText()));

        ivTemperatureIcon.setImageResource(UtilsProvider.getInstance()
                .getWeatherIcon(currentlyWeather.getIcon().getText()));

        tvDate.setText(UtilsProvider.getInstance().getDateText(currentlyWeather.getTime(), Constants.COMPLETE_DATE_FORMAT));

        tvTemperature.setText(UtilsProvider.getInstance()
                .getCelsiusTemperature(currentlyWeather.getTemperature()));

        tvTimezone.setText(timezone);

        tvSummary.setText(currentlyWeather.getSummary());
    }


}
