package com.example.gilado.senso.main.model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.gilado.senso.main.IMain.IMainModel;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.ISensorObserver;
import com.example.gilado.senso.main.model.sensor.SensorFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/23/2017.
 */

public class MainModel implements IMainModel {

    private Set<Integer> mSelectedSensorsId = new HashSet<>();
    private Observable<List<BaseSensor>> mSensorsObservable;
    private final PublishSubject<Integer> mSensorsSelectedSubject = PublishSubject.create();
    private final PublishSubject<Integer> mSensorsUnselectedSubject = PublishSubject.create();
    private IMainModelListener mMainPresenter;

    @Override
    public void setPresenter(IMainModelListener mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public Observable<List<BaseSensor>> getSensorListObservable(SensorManager sensorManager, ISensorObserver sensorObserver) {
        if (mSensorsObservable == null) {
            mSensorsObservable = Observable.create(e -> {
                List<Sensor>     deviceSensors    = sensorManager.getSensorList(Sensor.TYPE_ALL);
                List<BaseSensor> returnSensorList = new ArrayList<>();

                for (Sensor sensor : deviceSensors) {
                    BaseSensor baseSensor = SensorFactory.getSensor(sensor, sensorObserver);

                    if (baseSensor != null) {
                        boolean isSelected = mSelectedSensorsId.contains(baseSensor.getId());
                        baseSensor.setEnabled(isSelected);
                        returnSensorList.add(baseSensor);
                    }
                }
                e.onNext(returnSensorList);
            });
        }

        return mSensorsObservable;
    }

    @Override
    public PublishSubject<Integer> getSensorsSelectedSubject() {
        return mSensorsSelectedSubject;
    }

    @Override
    public PublishSubject<Integer> getSensorsUnselectedSubject() {
        return mSensorsUnselectedSubject;
    }

    @Override
    public void onSensorSelected(int sensorId) {
        mSelectedSensorsId.add(sensorId);
        getSensorsSelectedSubject().onNext(sensorId);
    }

    @Override
    public void onSensorUnselected(int sensorId) {
        if (mSelectedSensorsId.contains(sensorId)) {
            mSelectedSensorsId.remove(sensorId);
            getSensorsUnselectedSubject().onNext(sensorId);
        }
    }

    @Override
    public void onSensorAdded(BaseSensor sensor) {
        //TODO
    }

    @Override
    public void onSensorRemoved(BaseSensor sensor) {
        //TODO
    }

    @Override
    public void onSensorEvent(int sensorId, SensorEvent sensorEvent) {
        //TODO write to sensor file
    }


    //-----------------------------------------------------------------------------------------------
    //                          Private Methods
    //-----------------------------------------------------------------------------------------------

}
