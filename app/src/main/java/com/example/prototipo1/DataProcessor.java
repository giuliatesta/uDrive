package com.example.prototipo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/*
 Classe per la seconda Activity --> da usare quando si avvia la guida
 */

public class DataProcessor implements SensorEventListener {

    private SensorManager manager;
    public Sensor accelerometer;
    private Context context;
    private DataEventListener dataEventListener;

    public DataProcessor(Context context) {
        this.context = context;
        // impostazioni per il sensore
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==accelerometer.getType()) {       //Se gli eventi sono dell'accelerometro
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            DataEvent dataEvent = new DataEvent(Direction.LEFT, Acceleration.POSITIVE, 50);
            dataEventListener.onDataChanged(dataEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerListener(DataEventListener dataEventListener) {
        this.dataEventListener = dataEventListener;
    }
}
