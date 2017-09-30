package com.example.gilado.senso.main.IMain.Interactor;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.gilado.senso.main.IMain.IMainModel;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.ISensorObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gilado on 9/24/2017.
 */

public interface IMainModelInteractor {
    void setPresenter(IMainModel.IMainModelListener mainPresenter);

    Observable<List<BaseSensor>> getSensorListObservable(SensorManager sensorManager, ISensorObserver sensorObserver);

    PublishSubject<Integer> getSensorsSelectedSubject();

    PublishSubject<Integer> getSensorsUnselectedSubject();

    void onSensorAdded(BaseSensor sensor);

    void onSensorRemoved(BaseSensor sensor);

    void onSensorSelected(int sensorId);

    void onSensorUnselected(int sensorId);

    void onSensorEvent(int sensorId, SensorEvent sensorEvent);
}
