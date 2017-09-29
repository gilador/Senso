package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/25/2017.
 */

public class SensorFactory {

    public static BaseSensor getSensor(Sensor sensor, PublishSubject<SensorEvent> subject){
        switch (sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:{
                return new AccelerometerSensor(sensor, subject);
            }
            case Sensor.TYPE_AMBIENT_TEMPERATURE:{
                return new AmbientTempSensor(sensor, subject);
            }
            case Sensor.TYPE_LIGHT:{
                return new LightSensor(sensor, subject);
            }
            case Sensor.TYPE_PRESSURE:{
                return new PressureSensor(sensor, subject);
            }
            case Sensor.TYPE_PROXIMITY:{
                return new ProximitySensor(sensor, subject);
            }
            case Sensor.TYPE_MAGNETIC_FIELD:{
                return new MagnometerSensor(sensor, subject);
            }
            case Sensor.TYPE_GYROSCOPE:{
                return new GyroSensor(sensor, subject);
            }
            default:
                return null;
        }
    }
}
