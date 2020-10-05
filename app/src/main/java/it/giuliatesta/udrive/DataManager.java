package it.giuliatesta.udrive;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import it.giuliatesta.udrive.accelerometer.Acceleration;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
import it.giuliatesta.udrive.accelerometer.Direction;

/*
 Classe per la seconda Activity --> da usare quando si avvia la guida
 */

public class DataManager implements SensorEventListener {

    private SensorManager manager;
    public Sensor accelerometer;
    private Context context;
    private AccelerometerDataEventListener accelerometerDataEventListener;
    private DataProcessor accelerometerDataProcessor;

    public DataManager(Context context) {
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
            accelerometerDataEventListener.onDataChanged(accelerometerDataProcessor.calculateData(x,y,z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerListener(AccelerometerDataEventListener accelerometerDataEventListener) {
        this.accelerometerDataEventListener = accelerometerDataEventListener;
    }
}
