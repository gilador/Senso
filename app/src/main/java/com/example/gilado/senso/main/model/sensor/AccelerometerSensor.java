package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.gilado.senso.R;

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

    @Override
    protected String getProcessedData(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        return x + " , " + y + " , " + z;
    }

    @Override
    public int getIconResId() {
        return R.mipmap.accelometer;
    }

    @Override
    protected SensorEventListener getSensorListener() {
        return this;
    }
}
