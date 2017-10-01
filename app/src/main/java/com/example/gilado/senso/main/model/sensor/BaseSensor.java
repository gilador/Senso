package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/25/2017.
 */

abstract public class BaseSensor implements SensorEventListener {
    protected static final String TAG = BaseSensor.class.getSimpleName();

    private final PublishSubject<SensorEvent> mSensorEventSubject = PublishSubject.create();

    protected Sensor mSensor;

    private boolean mEnabled;

    //----------------------------------------------------------------------------------------------
    //                                 Constructors
    //----------------------------------------------------------------------------------------------

    public BaseSensor(Sensor sensor, ISensorObserver sensorObserver) {
        mSensor = sensor;
        mSensorEventSubject.subscribeOn(Schedulers.computation())
                .filter(this::filterEvent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorEvent -> sensorObserver.onSensorEvent(getId(), extractData(sensorEvent)));
    }

    //----------------------------------------------------------------------------------------------
    //                                 Abstract methods
    //----------------------------------------------------------------------------------------------

    protected abstract boolean applyThreshHold(SensorEvent ev);

    protected abstract String getProcessedData(SensorEvent sensorEvent);

    public abstract int getIconResId();


    //----------------------------------------------------------------------------------------------
    //                                 Impl SensorEventListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Todo
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(this.getClass().getSimpleName(), "onSensorChanged");
        mSensorEventSubject.onNext(sensorEvent);
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    //----------------------------------------------------------------------------------------------
    //                                 Public methods
    //----------------------------------------------------------------------------------------------
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public Sensor getSensor() {
        return mSensor;
    }

    public Integer getId() {
        return mSensor.getName().hashCode();
    }

    public void connect(SensorManager sensorManager, Handler handler) {
        handler.post(() -> {
            sensorManager.registerListener(getSensorListener(),
                    sensorManager.getDefaultSensor(mSensor.getType()),
                    SensorManager.SENSOR_DELAY_UI);
        });
    }

    public void disconnect(SensorManager sensorManager, Handler handler) {
        handler.post(() -> {
            sensorManager.unregisterListener(getSensorListener(), mSensor);
        });
    }

    //----------------------------------------------------------------------------------------------
    //                                 Protected methods
    //----------------------------------------------------------------------------------------------

    protected  SensorEventListener getSensorListener(){return this;}

    //----------------------------------------------------------------------------------------------
    //                                 Private methods
    //----------------------------------------------------------------------------------------------

    protected boolean filterEvent(SensorEvent ev) {
        return applyThreshHold(ev);
    }

    private String extractData(SensorEvent sensorEvent) {
        String processedData = getProcessedData(sensorEvent);
        return getSimpleName() + " : " + processedData;
    }

    private String getSimpleName() {
        return mSensor.getVendor() + " " + mSensor.getName();
    }
}
