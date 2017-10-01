package com.example.gilado.senso.main.model.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.gilado.senso.R;

/**
 * Created by gilado on 9/29/2017.
 */

public class MagnometerSensor extends BaseSensor {

    private       float gravity[]  = {1F, 1F, 1F};
    private       float magnetic[] = new float[3];

    public MagnometerSensor(Sensor sensor, ISensorObserver sensorObserver) {
        super(sensor, sensorObserver);
    }

    @Override
    protected boolean applyThreshHold(SensorEvent ev) {
        return true;
    }

    @Override
    protected String getProcessedData(SensorEvent sensorEvent) {
        magnetic[0] = sensorEvent.values[0];
        magnetic[1] = sensorEvent.values[1];
        magnetic[2] = sensorEvent.values[2];

        float[] R = new float[9];
        float[] I = new float[9];
        SensorManager.getRotationMatrix(R, I, gravity, magnetic);
        float[] A_D = sensorEvent.values.clone();
        float[] A_W = new float[3];
        A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
        A_W[1] = R[3] * A_D[0] + R[4] * A_D[1] + R[5] * A_D[2];
        A_W[2] = R[6] * A_D[0] + R[7] * A_D[1] + R[8] * A_D[2];


        String data = "Field X :" + A_W[0] + ", Y :" + A_W[1] + ", Z :" + A_W[2];

        return data;
    }

    @Override
    public int getIconResId() {
        return R.mipmap.magnometer;
    }
}
