package com.example.gilado.senso.main.view.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gilado.senso.R;
import com.example.gilado.senso.main.moduleInterface.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.github.yongjhih.mismeter.MisMeter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by gilado on 9/24/2017.
 */

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.SensorViewHolder> {

    private IMainView.IMainViewListener mPresenter;
    private List<BaseSensor>      mData                = new ArrayList<>(2);
    private Map<Integer, Integer> mTilePositionByIdMap = new Hashtable<>(8);
    private int[]   mTileColors;
    private boolean colorsInit;

    @Override
    public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!colorsInit) {
            initColors(parent.getContext());
        }

        LayoutInflater   inflater         = LayoutInflater.from(parent.getContext());
        View             v                = inflater.inflate(R.layout.sensor_tile, parent, false);
        SensorViewHolder sensorViewHolder = new SensorViewHolder(v);
        return sensorViewHolder;
    }

    @Override
    public void onBindViewHolder(SensorViewHolder holder, int position) {
        BaseSensor baseSensor = mData.get(position);
        mTilePositionByIdMap.put(baseSensor.getId(), position);

        //TODO
        holder.name.setText(baseSensor.getSensor().getName() + "\n" + printState(baseSensor.isEnabled()));
        holder.vendor.setText(baseSensor.getSensor().getVendor());
        holder.misMeter.setProgress(0.5F);
        holder.itemView.setOnClickListener(view -> {
            boolean newState = !baseSensor.isEnabled();
            if (newState) {
                mPresenter.onSensorTileSelected(baseSensor);
            } else {
                mPresenter.onSensorTileUnSelected(baseSensor);
            }
            holder.itemView.setSelected(!holder.itemView.isSelected());
        });

        setBackgroundColor(holder, position);
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

    public void updateState(Integer id, boolean isSelected) {
        int sensorPosition = mTilePositionByIdMap.get(id);
        mData.get(sensorPosition).setEnabled(isSelected);
        notifyItemChanged(sensorPosition);
    }

    public void setPresenter(IMainView.IMainViewListener presenter) {
        mPresenter = presenter;
    }


    //----------------------------------------------------------------------------------------------
    //                          Private methods
    //----------------------------------------------------------------------------------------------
    private void setBackgroundColor(SensorViewHolder holder, int position) {
        int selectedTilePosition = (position) % mTileColors.length;

        holder.itemView.setBackgroundColor(mTileColors[selectedTilePosition]);
    }

    private void initColors(Context context) {
        TypedArray ta = context.getResources().obtainTypedArray(R.array.tilesColors);
        mTileColors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            mTileColors[i] = ta.getColor(i, 0);
        }

        colorsInit = true;

        ta.recycle();
    }

    public class SensorViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView vendor;
        MisMeter misMeter;

        public SensorViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            vendor = view.findViewById(R.id.vendor);
            misMeter = view.findViewById(R.id.meter);

        }
    }

    //Temp code until
    private String printState(boolean enabled) {
        return enabled ? "ON" : "OFF";
    }
}
