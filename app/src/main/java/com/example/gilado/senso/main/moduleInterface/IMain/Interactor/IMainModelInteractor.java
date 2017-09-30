package com.example.gilado.senso.main.moduleInterface.IMain.Interactor;

import android.hardware.SensorManager;

import com.example.gilado.senso.main.moduleInterface.IMain.IMainModel;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.ISensorObserver;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by gilado on 9/24/2017.
 */

public interface IMainModelInteractor {
    void setPresenter(IMainModel.IMainModelListener mainPresenter);

    Observable<List<BaseSensor>> getSensorListObservable(SensorManager sensorManager, ISensorObserver sensorObserver);

    void onSensorSelected(int sensorId);

    void onSensorUnselected(int sensorId);

    void onSensorEvent(int sensorId, String sensorEvent);

    void onResume();

    void onPause();
}
