package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/29/2017.
 */

public class LightSensor extends BaseSensor {
    @Override
    protected void handleEvent(SensorEvent observable) {

    }

    @Override
    protected boolean applyThreshHold(SensorEvent ev) {
        return false;
    }

    public LightSensor(Sensor sensor, PublishSubject<SensorEvent> sensorEventSubject) {
        super(sensor, sensorEventSubject);
    }


}
