package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

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
        return null;
    }
}
