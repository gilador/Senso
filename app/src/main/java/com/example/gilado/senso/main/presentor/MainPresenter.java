package com.example.gilado.senso.main.presentor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import com.example.gilado.senso.main.IMain.IMainModel;
import com.example.gilado.senso.main.IMain.IMainPresenter;
import com.example.gilado.senso.main.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.model.sensor.SensorFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by gilado on 9/23/2017.
 */

public class MainPresenter implements IMainPresenter, SensorEventListener {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private final Context       mContext;
    private final SensorManager mSensorManager;
    private       Handler       mHandler;
    private       IMainModel    mMainModel;
    private       IMainView     mMainView;

    private final PublishSubject<SensorEvent>           mSensorEventSubject    = PublishSubject.create();
    private final PublishSubject<Pair<Sensor, Integer>> mSensorAccuracySubject = PublishSubject.create();

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
        Observable<Collection<BaseSensor>> sensorsObservable = getBaseSensorsObservable();
        sensorsObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseSensors -> {
                    Log.d(TAG, "onViewResume->sensorsObservable->doOnNext->baseSensors size: " + baseSensors.size());
                    mMainView.showSensorList(new ArrayList(baseSensors));
                });
    }

    @Override
    public void onViewPause() {

    }

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void onSensorTileSelected(BaseSensor sensor) {
        mMainModel.onSensorAdded(sensor);
    }

    @Override
    public void onSensorTileUnSelected(BaseSensor sensor) {
        mMainModel.onSensorRemoved(sensor);

    }

    //----------------------------------------------------------------------------------------------
    //                                 Impl SensorEventListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        mSensorAccuracySubject.onNext(new Pair<>(sensor, i));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mSensorEventSubject.onNext(sensorEvent);
    }

    //----------------------------------------------------------------------------------------------
    //                                 private methods
    //----------------------------------------------------------------------------------------------

    private Observable<Collection<BaseSensor>> getBaseSensorsObservable() {
        return Observable.zip(
                mMainModel.getGeneralSensorList(mSensorManager, mSensorEventSubject),
                mMainModel.getSelectedSensors(),
                (generalSensorsList, selectedSensorsList) -> {
                    Map<Integer, BaseSensor> sensorsMap = new LinkedHashMap<>();
                    for (BaseSensor baseSensor : generalSensorsList) {
                        sensorsMap.put(baseSensor.getId(), baseSensor);
                    }

                    for (BaseSensor baseSensor : selectedSensorsList) {
                        boolean isSelected = sensorsMap.containsKey(baseSensor.getSensor().getType());
                        sensorsMap.get(baseSensor.getId()).setEnabled(isSelected);
                    }

                    return sensorsMap.values();
                });
    }

//    private Single<List<BaseSensor>> getGeneralSensorList() {
//        Single<List<BaseSensor>> listSingle = Single.create((SingleOnSubscribe<List<BaseSensor>>) e -> {
//            SensorManager    sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
//            List<Sensor>     sensors       = sensorManager.getSensorList(Sensor.TYPE_ALL);
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
