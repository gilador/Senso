package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/25/2017.
 */

abstract public class BaseSensor implements SensorEventListener {
    public static final String TAG = BaseSensor.class.getSimpleName();

    private final PublishSubject<SensorEvent> mSensorEventSubject = PublishSubject.create();

    private Sensor mSensor;

    private boolean mEnabled;

    //----------------------------------------------------------------------------------------------
    //                                 Constructors
    //----------------------------------------------------------------------------------------------

    public BaseSensor(Sensor sensor, ISensorObserver sensorObserver) {
        mSensor = sensor;
        mSensorEventSubject.doOnNext(sensorEvent -> sensorObserver.onSensorEvent(getId(), sensorEvent)).filter(this::filterEvent);
    }

    //----------------------------------------------------------------------------------------------
    //                                 Abstract methods
    //----------------------------------------------------------------------------------------------

    protected abstract boolean applyThreshHold(SensorEvent ev);

    //----------------------------------------------------------------------------------------------
    //                                 Impl SensorEventListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Todo
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "onSensorChanged");
        mSensorEventSubject.onNext(sensorEvent);
    }

    //----------------------------------------------------------------------------------------------
    //                                 Public methods
    //----------------------------------------------------------------------------------------------
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public boolean isEnabled() {
        return mEnabled;
    }
    public Sensor getSensor() {
        return mSensor;
    }

    public Integer getId() {
        return mSensor.getName().hashCode();
    }

    public boolean connect(SensorManager sensorManager) {
        return sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(mSensor.getType()),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void disconnect(SensorManager sensorManager) {
        sensorManager.unregisterListener(this);
    }

    //----------------------------------------------------------------------------------------------
    //                                 Private methods
    //----------------------------------------------------------------------------------------------

    private boolean filterEvent(SensorEvent ev) {
        return ev.sensor.getType() == mSensor.getType() && applyThreshHold(ev);
    }
}
