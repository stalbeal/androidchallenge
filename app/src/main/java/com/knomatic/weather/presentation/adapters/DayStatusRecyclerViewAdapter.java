package com.knomatic.weather.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.zetterstrom.com.forecast.models.DataPoint;

import com.knomatic.weather.R;
import com.knomatic.weather.presentation.holders.DayStatusItemViewHolder;
import com.knomatic.weather.providers.UtilsProvider;
import com.knomatic.weather.utils.Constants;

import java.util.List;

/**
 * Created by stephany.berrio on 28/02/17.
 */
public class DayStatusRecyclerViewAdapter extends RecyclerView.Adapter<DayStatusItemViewHolder> {

    List<DataPoint> dataPointList;

    public DayStatusRecyclerViewAdapter(List<DataPoint> dataPointList) {
        this.dataPointList = dataPointList;

    }

    @Override
    public DayStatusItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_status_item, parent, false);

        return new DayStatusItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DayStatusItemViewHolder holder, int position) {
        final DataPoint dataPoint = dataPointList.get(position);
        holder.getIvTemperatureIcon().setImageResource(UtilsProvider.getInstance()
                .getWeatherIcon(dataPoint.getIcon().getText()));

        holder.getTvDate().setText(String.valueOf(UtilsProvider.getInstance()
                .getDateText(dataPoint.getTime(), Constants.DAY_NAME_FORMAT)));

        if (dataPoint.getTemperature() == null && dataPoint.getTemperatureMax() == null) {
            holder.getTvTemperature().setText("N/A");
        } else if (dataPoint.getTemperature() == null) {
            holder.getTvTemperature().setText(UtilsProvider.getInstance()
                    .getCelsiusTemperature(dataPoint.getApparentTemperatureMax()) +
                    "/" + UtilsProvider.getInstance()
                    .getCelsiusTemperature(dataPoint.getTemperatureMin()));
        }


    }

    @Override
    public int getItemCount() {
        return this.dataPointList.size();
    }
}
