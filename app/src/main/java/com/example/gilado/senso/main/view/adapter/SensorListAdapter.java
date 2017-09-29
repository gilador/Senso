package com.example.gilado.senso.main.view.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gilado.senso.R;
import com.example.gilado.senso.main.model.sensor.BaseSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gilado on 9/24/2017.
 */

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.SensorViewHolder> {

    private List<BaseSensor> mData = new ArrayList<>(2);
    private int[] mTileColors;
    private boolean colorsInit;

    @Override
    public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(!colorsInit){
            initColors(parent.getContext());
        }

        LayoutInflater   inflater         = LayoutInflater.from(parent.getContext());
        View             v                = inflater.inflate(R.layout.sensor_tile, parent, false);
        SensorViewHolder sensorViewHolder = new SensorViewHolder(v);
        return sensorViewHolder;
    }

    @Override
    public void onBindViewHolder(SensorViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getSensor().getName());
        setBackgroundColor(holder, position);
//        holder.itemView.setBackgroundResource(R.color.tile2);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //----------------------------------------------------------------------------------------------
    //                          Public methods
    //----------------------------------------------------------------------------------------------
    public void setList(List<BaseSensor> sensors) {
        mData = sensors;
        notifyDataSetChanged();
    }


    public class SensorViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public SensorViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
        }

    }

    //----------------------------------------------------------------------------------------------
    //                          Private methods
    //----------------------------------------------------------------------------------------------
    private void setBackgroundColor(SensorViewHolder holder, int position) {
        int selectedTilePosition = (position) % mTileColors.length;

        holder.itemView.setBackgroundColor(mTileColors[selectedTilePosition]);
    }

    private void initColors(Context context){
        TypedArray ta     = context.getResources().obtainTypedArray(R.array.tilesColors);
        mTileColors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            mTileColors[i] = ta.getColor(i, 0);
        }

        colorsInit = true;

       ta.recycle();
    }
}
