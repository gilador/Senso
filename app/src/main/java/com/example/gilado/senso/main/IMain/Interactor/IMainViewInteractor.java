package com.example.gilado.senso.main.IMain.Interactor;

import android.hardware.SensorEvent;

import com.example.gilado.senso.main.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;

import java.util.List;

/**
 * Created by gilado on 9/24/2017.
 */

public interface IMainViewInteractor {
    void setPresenter(IMainView.IMainViewListener presenter);

    void onSensorListChange(List<BaseSensor> sensors);

    void onSensorEvent(int sensorId, SensorEvent sensorEvent);

    void onTileStateChanged(Integer id, boolean isSelected);
}
