package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.example.gilado.senso.R;

/**
 * Created by gilado on 9/29/2017.
 */

public class AmbientTempSensor extends BaseSensor {


    public AmbientTempSensor(Sensor sensor, ISensorObserver sensorObserver) {
        super(sensor, sensorObserver);
    }

    @Override
    protected boolean applyThreshHold(SensorEvent ev) {
        return true;
    }

    @Override
    protected String getProcessedData(SensorEvent sensorEvent) {
        float ambient_temperature = sensorEvent.values[0];
        return ambient_temperature + "";
    }

    @Override
    public int getIconResId() {
        return R.mipmap.temperature;
    }
}
