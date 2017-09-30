package com.example.gilado.senso.main.model.fileModel;

import android.content.Context;
import android.hardware.Sensor;
import android.support.annotation.NonNull;

import com.example.gilado.senso.main.model.MainModel;
import com.example.gilado.senso.main.model.fileModel.filePublisher.FireBasePublisher;
import com.example.gilado.senso.main.model.fileModel.filePublisher.IFilePublisher;
import com.example.gilado.senso.main.model.sensor.BaseSensor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by gilado on 9/30/2017.
 */

public class FileMainModel extends MainModel {

    private final IFilePublisher                 publisher;
    private       Map<Integer, FileOutputStream> mWritersMap;

    public FileMainModel(FireBasePublisher fireBasePublisher) {
        super();
        publisher = fireBasePublisher;
    }


    @Override
    protected void onSensorStart(int sensorId) {
        if (mWritersMap == null) {
            mWritersMap = new Hashtable<>();
        }

        try {
            mWritersMap.put(sensorId, mMainPresenter.getContext().openFileOutput(getInternalFileName(sensorId), Context.MODE_APPEND));
        } catch (IOException e) {
            //TODO notify UI filOe failed to create
            e.printStackTrace();
        }
    }

    @Override
    protected void onSensorStop(int sensorId) {
        FileOutputStream writer = mWritersMap.get(sensorId);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String publishSensorData(BaseSensor sensor) throws FileNotFoundException {
        FileInputStream reader;
        reader = mMainPresenter.getContext().openFileInput(getInternalFileName(sensor.getId()));


        publisher.publish(reader, getExternalFileName(sensor.getSensor()), getInternalFileName(sensor.getId()));
        try {
            return (getSensorData(sensor.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "test";
    }

    @Override
    public void onSensorEvent(int sensorId, String sensorEvent) {
        FileOutputStream writer = mWritersMap.get(sensorId);
        try {
            synchronized (this) {
                writer.write(editEventText(sensorEvent));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                 Private Methods
    //----------------------------------------------------------------------------------------------

    private String getSensorData(int sensorId) throws IOException {
        FileInputStream reader;
        reader = mMainPresenter.getContext().openFileInput(getInternalFileName(sensorId));

        StringBuilder builder = new StringBuilder("");

        byte[] buffer = new byte[1024];

        int n;
        while ((n = reader.read(buffer)) != -1) {
            builder.append(new String(buffer, 0, n));
        }

        return builder.toString();
    }

    private byte[] editEventText(String sensorEvent) {
        String time = getTime();
        return ("| " + time + " : " + sensorEvent + " |").getBytes();
    }

    private String getTime() {
        Calendar   cal              = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date       currentLocalTime = cal.getTime();
        DateFormat date             = new SimpleDateFormat("HH:mm a:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    @NonNull
    private String getInternalFileName(int sensorId) {
        return sensorId + ".txt";
    }

    @NonNull
    private String getExternalFileName(Sensor sensor) {
        return getTime() + " " + sensor.getName() + ".txt";
    }
}
