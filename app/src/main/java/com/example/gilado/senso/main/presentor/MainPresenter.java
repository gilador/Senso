package com.example.gilado.senso.main.presentor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import com.example.gilado.senso.main.IMain.IMainModel;
import com.example.gilado.senso.main.IMain.IMainPresenter;
import com.example.gilado.senso.main.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.ISensorObserver;
import com.example.gilado.senso.main.view.MainView;

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
        mMainModel.getSensorListObservable(mSensorManager, this).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseSensors -> {
                    Log.d(TAG, "onViewPause->mMainModel.getSensorListObservable->subscribe->baseSensors size: " + baseSensors.size());
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
        sensor.connect(mSensorManager);
        mMainView.onTileStateChanged(sensor.getId(), true);
    }

    @Override
    public void onSensorTileUnSelected(BaseSensor sensor) {
        sensor.disconnect(mSensorManager);
        sensor.setEnabled(false);
        mMainModel.onSensorUnselected(sensor.getId());
        mMainView.onTileStateChanged(sensor.getId(), false);


    }

    //----------------------------------------------------------------------------------------------
    //                                 Impl ISensorObserver
    //----------------------------------------------------------------------------------------------
    @Override
    public void onSensorEvent(int sensorId, SensorEvent sensorEvent) {
        mMainView.onSensorEvent(sensorId, sensorEvent);
        mMainModel.onSensorEvent(sensorId, sensorEvent);
    }

    //----------------------------------------------------------------------------------------------
    //                                 private methods
    //----------------------------------------------------------------------------------------------

    private void connectSensors(List<BaseSensor> sensors) {
        for (BaseSensor sensor : sensors) {
            if (sensor.isEnabled()) {
                sensor.connect(mSensorManager);
            }
        }
    }

    private void disconnectSensors(List<BaseSensor> sensors) {
        for (BaseSensor sensor : sensors) {
            if (sensor.isEnabled()) {
                sensor.disconnect(mSensorManager);
            }
        }
    }

//    private Observable<Collection<BaseSensor>> getBaseSensorsObservable() {
//        return Observable.zip(
//                mMainModel.getSensorListObservable(mSensorManager, this),
//                mMainModel.getSelectedSensors(),
//                (generalSensorsList, selectedSensorsList) -> {
//                    Map<Integer, BaseSensor> sensorsMap = new LinkedHashMap<>();
//                    for (BaseSensor baseSensor : generalSensorsList) {
//                        sensorsMap.put(baseSensor.getId(), baseSensor);
//                    }
//
//                    for (BaseSensor baseSensor : selectedSensorsList) {
//                        boolean isSelected = sensorsMap.containsKey(baseSensor.getSensor().getType());
//                        sensorsMap.get(baseSensor.getId()).setEnabled(isSelected);
//                    }
//
//                    return sensorsMap.values();
//                });
//    }

//    private Single<List<BaseSensor>> getSensorListObservable() {
//        Single<List<BaseSensor>> listSingle = Single.create((SingleOnSubscribe<List<BaseSensor>>) e -> {
//            SensorManager    sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
//            List<Sensor>     sensors       = sensorManager.getSensorListObservable(Sensor.TYPE_ALL);
//            List<BaseSensor> baseSensors   = new ArrayList<>();
//            for (Sensor sensor : sensors) {
//                BaseSensor baseSensor = SensorFactory.getSensor(sensor, mSensorEventSubject);
//                //TODO register sensors
////                sensorManager.registerListener(this,
////                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
////                        SensorManager.SENSOR_DELAY_NORMAL);
//                if(baseSensor != null) {
//                    baseSensors.add(baseSensor);
//                }
//            }
//
//            e.onSuccess(baseSensors);
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//        return listSingle;
//    }
}
