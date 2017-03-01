package com.knomatic.weather.presentation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.zetterstrom.com.forecast.models.DataBlock;

import com.knomatic.weather.R;
import com.knomatic.weather.presentation.adapters.DayStatusRecyclerViewAdapter;
import com.knomatic.weather.utils.Constants;

/**
 * Fragment to display weather on the week
 */
public class WeekWeatherFragment extends Fragment {


    private DataBlock weekStatusWeather;
    private RecyclerView rvWeekStatus;
    private DayStatusRecyclerViewAdapter dayStatusRecylerViewAdapter;

    public WeekWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeekStatusFragment.
     * @param daily
     */
    public static WeekWeatherFragment newInstance(DataBlock daily) {
        WeekWeatherFragment fragment = new WeekWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DAILY_WEATHER, daily);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weekStatusWeather = (DataBlock) getArguments().getSerializable(Constants.DAILY_WEATHER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_weather, container, false);
        initViewComponents(view);
        return view;
    }

    /**
     * Method to init the view components and build the
     * view for the activity
     */
    private void initViewComponents(View view){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        if(getContext().getResources().getConfiguration().orientation == 2){
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        dayStatusRecylerViewAdapter = new DayStatusRecyclerViewAdapter(weekStatusWeather.getDataPoints());
        rvWeekStatus  = (RecyclerView) view.findViewById(R.id.rv_week_status);
        rvWeekStatus.setLayoutManager(layoutManager);
        rvWeekStatus.setAdapter(dayStatusRecylerViewAdapter);
    }

}