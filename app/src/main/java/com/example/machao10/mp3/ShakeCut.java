package com.example.machao10.mp3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by machao10 on 2016/4/28.
 */
public class ShakeCut {
    public static final int SHAKE_POWER = 0;
    public static final int SHAKE_NORMAL = 1;
    public static final int SHAKE_TENDER = 2;

    private int shakeLevel = SHAKE_NORMAL;
    private SensorManager sensorManager;
    private Sensor sensor;

    public ShakeCut(Context context, int shakeLevel) {
        this.shakeLevel = shakeLevel;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void setShakeCut(boolean state) {

    }
}
