package com.example.gilado.senso.main.IMain;

import com.example.gilado.senso.main.IMain.Interactor.IMainViewInteractor;
import com.example.gilado.senso.main.model.sensor.BaseSensor;

/**
 * Created by gilado on 9/23/2017.
 */

public interface IMainView extends IMainViewInteractor{

    interface IMainViewListener {
        void onViewCreate();

        void onViewResume();

        void onViewPause();

        void onViewDestroy();

        void onSensorTileSelected(BaseSensor baseSensor);

        void onSensorTileUnSelected(BaseSensor baseSensor);
    }
}
