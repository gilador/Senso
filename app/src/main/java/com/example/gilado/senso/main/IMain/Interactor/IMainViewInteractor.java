package com.example.gilado.senso.main.IMain.Interactor;

import com.example.gilado.senso.main.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;

import java.util.List;

/**
 * Created by gilado on 9/24/2017.
 */

public interface IMainViewInteractor {
    void setPresenter(IMainView.IMainViewListener presenter);
    void showSensorList(List<BaseSensor> sensors);

    void onSelectedSensorLoad(List<BaseSensor> baseSensors);
}
