package com.example.gilado.senso.main.moduleInterface.IMain;

import android.content.Context;

import com.example.gilado.senso.main.moduleInterface.IMain.Interactor.IMainModelInteractor;

/**
 * Created by gilado on 9/23/2017.
 */

public interface IMainModel extends IMainModelInteractor{

    interface IMainModelListener {
        Context getContext();

        void onSensorDataPublished(String sensorData);
    }
}
