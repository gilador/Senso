package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.example.gilado.senso.R;

/**
 * Created by gilado on 9/29/2017.
 */

public class ProximitySensor extends BaseSensor {

    public ProximitySensor(Sensor sensor, ISensorObserver sensorObserver) {
        super(sensor, sensorObserver);
    }

    @Override
    protected boolean applyThreshHold(SensorEvent ev) {
        return false;
    }

    @Override
    protected String getProcessedData(SensorEvent sensorEvent) {
        return null;
    }

    @Override
    public int getIconResId() {
        return R.mipmap.proximity;
    }
}
