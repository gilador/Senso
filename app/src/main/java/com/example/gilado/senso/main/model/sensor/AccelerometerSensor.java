package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by gilado on 9/25/2017.
 */

public class AccelerometerSensor extends BaseSensor {


    public AccelerometerSensor(Sensor sensor, ISensorObserver sensorObserver) {
        super(sensor, sensorObserver);

    }

    @Override
    protected boolean applyThreshHold(SensorEvent ev) {
        float[] values = ev.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        if (accelationSquareRoot >= 2) {
            return true;
        }

        Log.d(TAG, "filtered!");

        return false;
    }

}
