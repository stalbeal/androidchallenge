package com.knomatic.weather.presentation.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.knomatic.weather.R;

/**
 * Created by stephany.berrio on 28/02/17.
 */
public class DayStatusItemViewHolder extends RecyclerView.ViewHolder{

    private TextView tvTemperature;
    private TextView tvDate;
    private ImageView ivTemperatureIcon;

    public DayStatusItemViewHolder(View itemView) {
        super(itemView);

        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvTemperature = (TextView) itemView.findViewById(R.id.tv_temperature);
        ivTemperatureIcon = (ImageView) itemView.findViewById(R.id.iv_temperature_icon);
    }

    public TextView getTvTemperature() {
        return tvTemperature;
    }

    public TextView getTvDate() {
        return tvDate;
    }

    public ImageView getIvTemperatureIcon() {
        return ivTemperatureIcon;
    }
}
