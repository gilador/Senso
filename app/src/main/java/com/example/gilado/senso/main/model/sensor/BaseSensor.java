package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/25/2017.
 */

abstract public class BaseSensor {
    protected abstract void handleEvent(SensorEvent observable);

    protected abstract boolean applyThreshHold(SensorEvent ev);

    private Sensor mSensor;

    private boolean mEnabled;


    private final Observable<SensorEvent> mFilteredObservable;

    public BaseSensor(Sensor sensor, PublishSubject<SensorEvent> sensorEventSubject) {
        mSensor = sensor;
        mFilteredObservable = sensorEventSubject.filter(this::filterEvent);
        mFilteredObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleEvent);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public Sensor getSensor() {
        return mSensor;
    }

    public Integer getId() {
        return mSensor.getName().hashCode();
    }

    private boolean filterEvent(SensorEvent ev) {
        return ev.sensor.getType() == mSensor.getType() && applyThreshHold(ev);
    }

}
