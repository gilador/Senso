package com.example.gilado.senso.main.model.sensor;

import android.hardware.SensorEvent;

/**
 * Created by gilado on 9/30/2017.
 */

public interface ISensorObserver {
    void onSensorEvent(int sensorId, SensorEvent sensorEvent);
}
