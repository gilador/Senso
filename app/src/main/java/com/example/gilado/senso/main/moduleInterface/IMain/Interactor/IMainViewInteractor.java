package com.example.gilado.senso.main.moduleInterface.IMain.Interactor;

import com.example.gilado.senso.main.moduleInterface.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;

import java.util.List;

/**
 * Created by gilado on 9/24/2017.
 */

public interface IMainViewInteractor {
    void setPresenter(IMainView.IMainViewListener presenter);

    void onSensorListChange(List<BaseSensor> sensors);

    void onSensorEvent(int sensorId, String sensorEvent);

    void onTileStateChanged(Integer id, boolean isSelected);
}
