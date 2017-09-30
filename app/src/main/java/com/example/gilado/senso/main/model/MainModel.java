package com.example.gilado.senso.main.model;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.ISensorObserver;
import com.example.gilado.senso.main.model.sensor.SensorFactory;
import com.example.gilado.senso.main.moduleInterface.IMain.IMainModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by gilado on 9/23/2017.
 */

public abstract class MainModel implements IMainModel {

    protected IMainModelListener mMainPresenter;
    private Set<Integer> mSelectedSensorsId = new HashSet<>();
    private Observable<List<BaseSensor>> mSensorsObservable;

    //----------------------------------------------------------------------------------------------
    //                                 Abstract methods
    //----------------------------------------------------------------------------------------------
    protected abstract void onSensorStart(int sensorId);

    protected abstract void onSensorStop(int sensorId);

    protected abstract String publishSensorData(BaseSensor sensorId) throws FileNotFoundException;

    //----------------------------------------------------------------------------------------------
    //                                 Impl IMainModel
    //----------------------------------------------------------------------------------------------
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
    public void onSensorSelected(int sensorId) {
        mSelectedSensorsId.add(sensorId);
        onSensorStart(sensorId);
    }

    @Override
    public void onSensorUnselected(BaseSensor sensor) {
        if (mSelectedSensorsId.contains(sensor)) {
            mSelectedSensorsId.remove(sensor);
        }
        onSensorStop(sensor.getId());
        String publishedData = "";
        try {
            publishedData = publishSensorData(sensor);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mMainPresenter.onSensorDataPublished(publishedData);

    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        //TODO close all writers
    }
}
