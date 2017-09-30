package com.example.gilado.senso.main.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.FileInputStream;
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

    private Map<Integer, FileOutputStream> mWritersMap;


    @Override
    protected void onSensorStart(int sensorId) {
        if(mWritersMap == null){
            mWritersMap = new Hashtable<>();
        }

        try {
            mWritersMap.put(sensorId, mMainPresenter.getContext().openFileOutput(getFileName(sensorId), Context.MODE_APPEND));
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
            String sensorData = getSensorData(sensorId);
            publishSensorData(sensorData);


        } catch (IOException e) {
            e.printStackTrace();
        }
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
        reader = mMainPresenter.getContext().openFileInput(getFileName(sensorId));

        StringBuilder builder = new StringBuilder("");

        byte[] buffer = new byte[1024];

        int n;
        while ((n = reader.read(buffer)) != -1)
        {
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
    private String getFileName(int sensorId) {
        return sensorId+".txt";
    }
}
