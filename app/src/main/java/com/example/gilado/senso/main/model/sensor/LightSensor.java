package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.example.gilado.senso.R;

/**
 * Created by gilado on 9/29/2017.
 */

public class LightSensor extends BaseSensor {

    public LightSensor(Sensor sensor, ISensorObserver sensorObserver) {
        super(sensor, sensorObserver);
    }

    @Override
    protected boolean applyThreshHold(SensorEvent ev) {
        return false;
    }

    @Override
    protected String getProcessedData(SensorEvent sensorEvent) {
        return sensorEvent.values[0] + "";
    }

    @Override
    public int getIconResId() {
        return R.mipmap.light;
    }
}
