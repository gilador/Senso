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

    @Override
    protected String getProcessedData(SensorEvent sensorEvent) {
        String data = "Orientation X (Roll) :" +
                Float.toString(sensorEvent.values[2]) +
                "," +
                "Orientation Y (Pitch) :" +
                Float.toString(sensorEvent.values[1]) +
                ", " +
                "Orientation Z (Yaw) :" +
                Float.toString(sensorEvent.values[0]);
        return data;
    }
}
