package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/25/2017.
 */

public class SensorFactory {

    public static BaseSensor getSensor(Sensor sensor, ISensorObserver sensorObserver){
        switch (sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:{
                return new AccelerometerSensor(sensor, sensorObserver);
            }
            case Sensor.TYPE_AMBIENT_TEMPERATURE:{
                return new AmbientTempSensor(sensor, sensorObserver);
            }
            case Sensor.TYPE_LIGHT:{
                return new LightSensor(sensor, sensorObserver);
            }
            case Sensor.TYPE_PRESSURE:{
                return new PressureSensor(sensor, sensorObserver);
            }
            case Sensor.TYPE_PROXIMITY:{
                return new ProximitySensor(sensor, sensorObserver);
            }
            case Sensor.TYPE_MAGNETIC_FIELD:{
                return new MagnometerSensor(sensor, sensorObserver);
            }
            case Sensor.TYPE_GYROSCOPE:{
                return new GyroSensor(sensor, sensorObserver);
            }
            default:
                return null;
        }
    }
}
