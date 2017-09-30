package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by gilado on 9/29/2017.
 */

public class GyroSensor extends BaseSensor {

    public GyroSensor(Sensor sensor, ISensorObserver sensorObserver) {
        super(sensor, sensorObserver);
    }

    @Override
    protected boolean applyThreshHold(SensorEvent ev) {
        return false;
    }
}
