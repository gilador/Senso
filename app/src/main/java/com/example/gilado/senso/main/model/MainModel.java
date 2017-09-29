package com.example.gilado.senso.main.model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.gilado.senso.main.IMain.IMainModel;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.SensorFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/23/2017.
 */

public class MainModel implements IMainModel {

    private Map<Integer, BaseSensor> mBaseSensorMap = new Hashtable<>();
    private Observable<List<BaseSensor>> mSelectedSensorsObservable;
    private Observable<List<BaseSensor>> mGeneralSensorsObservable;
    private IMainModelListener           mMainPresenter;

    @Override
    public void setPresenter(IMainModelListener mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public Observable<List<BaseSensor>> getSelectedSensors() {
        if (mSelectedSensorsObservable == null) {
            mSelectedSensorsObservable = Observable.create(e -> {
                e.onNext(new ArrayList<>(mBaseSensorMap.values()));
            });
        }
        return mSelectedSensorsObservable;
    }

    @Override
    public Observable<List<BaseSensor>> getGeneralSensorList(SensorManager sensorManager, PublishSubject<SensorEvent> sensorEventSubject) {
        if (mGeneralSensorsObservable == null) {
            mGeneralSensorsObservable = Observable.create(e -> {
                List<Sensor>     sensors     = sensorManager.getSensorList(Sensor.TYPE_ALL);
                List<BaseSensor> baseSensors = new ArrayList<>();
                for (Sensor sensor : sensors) {
                    BaseSensor baseSensor = SensorFactory.getSensor(sensor, sensorEventSubject);
                    //TODO register sensors
//                sensorManager.registerListener(this,
//                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                        SensorManager.SENSOR_DELAY_NORMAL);
                    if (baseSensor != null) {
                        baseSensors.add(baseSensor);
                    }
                }

                e.onNext(baseSensors);
            });
        }

        return mGeneralSensorsObservable;
    }

    @Override
    public void onSensorAdded(BaseSensor sensor) {
        if (!mBaseSensorMap.containsKey(sensor.getSensor().getType())) {
            mBaseSensorMap.put(sensor.getId(), sensor);
        }
    }

    @Override
    public void onSensorRemoved(BaseSensor sensor) {
        if (mBaseSensorMap.containsKey(sensor.getSensor().getType())) {
            mBaseSensorMap.remove(sensor.getId());
        }
    }

    @Override
    public void onSensorSelected(BaseSensor sensor) {
        if (hasSensor(sensor)) {
            //TODO change to notify for single sensor change
            mSelectedSensorsObservable = Observable.create(e -> e.onNext(new ArrayList<>(mBaseSensorMap.values())));
        }
    }

    @Override
    public void onSensorUnselected(BaseSensor sensor) {
        if (hasSensor(sensor)) {
            //TODO change to notify for single sensor change
            mSelectedSensorsObservable = Observable.create(e -> e.onNext(new ArrayList<>(mBaseSensorMap.values())));

        }
    }


    //-----------------------------------------------------------------------------------------------
    //                          Private Methods
    //-----------------------------------------------------------------------------------------------

    private boolean hasSensor(BaseSensor sensor) {
        return mBaseSensorMap.containsKey(sensor.getId());
    }
}
