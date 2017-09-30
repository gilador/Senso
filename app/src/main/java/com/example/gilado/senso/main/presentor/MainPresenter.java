package com.example.gilado.senso.main.presentor;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import com.example.gilado.senso.main.moduleInterface.IMain.IMainModel;
import com.example.gilado.senso.main.moduleInterface.IMain.IMainPresenter;
import com.example.gilado.senso.main.moduleInterface.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.ISensorObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gilado on 9/23/2017.
 */

public class MainPresenter implements IMainPresenter, ISensorObserver {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private final Context       mContext;
    private final SensorManager mSensorManager;
    private       Handler       mHandler;
    private       IMainModel    mMainModel;
    private       IMainView     mMainView;


    //TODO:
//    CompositeDisposable disposable = new CompositeDisposable();


    public MainPresenter(IMainModel model, IMainView view, Handler handler, Context applicationContext, SensorManager sensorManager) {
        mMainModel = model;
        mMainView = view;
        mHandler = handler;
        mContext = applicationContext;
        mSensorManager = sensorManager;

        mMainView.setPresenter(this);
        mMainModel.setPresenter(this);
    }

    //----------------------------------------------------------------------------------------------
    //                                 Impl IMainView.IMainViewListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onViewCreate() {

    }

    @Override
    public void onViewResume() {
        mMainModel.onResume();//TODO maybe should be in base class
        mMainModel.getSensorListObservable(mSensorManager, this).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseSensors -> {
                    Log.d(TAG, "onViewResume->mMainModel.getSensorListObservable->subscribe->baseSensors size: " + baseSensors.size());
                    mMainView.onSensorListChange(baseSensors);
                    connectSensors(baseSensors);
                });
    }

    @Override
    public void onViewPause() {
        mMainModel.onPause();  //TODO maybe should be in base class
        mMainModel.getSensorListObservable(mSensorManager, this).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseSensors -> {
                    Log.d(TAG, "onViewPause->mMainModel.getSensorListObservable->subscribe->baseSensors size: " + baseSensors.size());
                    //TODO: BUG - not working - it seems like two different instances
                    disconnectSensors(baseSensors);
                });
    }

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void  onSensorTileSelected(BaseSensor sensor) {
        mMainModel.onSensorSelected(sensor.getId());
        sensor.setEnabled(true);
        sensor.connect(mSensorManager, mHandler);
        mMainView.onTileStateChanged(sensor.getId(), true);
    }

    @Override
    public void onSensorTileUnSelected(BaseSensor sensor) {
        sensor.disconnect(mSensorManager, mHandler);
        sensor.setEnabled(false);
        mMainModel.onSensorUnselected(sensor);
        mMainView.onTileStateChanged(sensor.getId(), false);


    }

    //----------------------------------------------------------------------------------------------
    //                                 Impl ISensorObserver
    //----------------------------------------------------------------------------------------------
    @Override
    public void onSensorEvent(int sensorId, String sensorEvent) {
        mMainView.onSensorEvent(sensorId, sensorEvent);
        mMainModel.onSensorEvent(sensorId, sensorEvent);
    }

    //----------------------------------------------------------------------------------------------
    //                                 private methods
    //----------------------------------------------------------------------------------------------

    private void connectSensors(List<BaseSensor> sensors) {
        for (BaseSensor sensor : sensors) {
            if (sensor.isEnabled()) {
                sensor.connect(mSensorManager, mHandler);
            }
        }
    }

    private void disconnectSensors(List<BaseSensor> sensors) {
        for (BaseSensor sensor : sensors) {
            if (sensor.isEnabled()) {
                sensor.disconnect(mSensorManager, mHandler);
            }
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onSensorDataPublished(String sensorData) {
        Log.i(TAG, "onSensorDataPublished -> sensor data: " + sensorData);
    }
}
