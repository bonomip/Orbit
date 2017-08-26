package com.example.paolobonomi.eattheball.Activity.Engine;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/*
 * Created by Paolo Bonomi on 10/05/2017.
 */

public class GameSensor implements SensorEventListener {
    public static float x, y, cx, cy;

    //SENSOR EVENT LISTENER METHODS
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //in this case our game is full screen so x e y are inverted
            if(!GameContract.REVERSE_ORIENTATION) {
                y = -(event.values[0]);
                x = event.values[1];
            } else {
                y = event.values[0];
                x = -(event.values[1]);
            }
        }
    }

    public static void calibrate(){
        cx = x; cy = y;
    }
}
