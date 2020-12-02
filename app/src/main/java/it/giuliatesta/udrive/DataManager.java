package it.giuliatesta.udrive;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

/*
 Classe per la gestione degli eventi causati dalla modifica dei valori del sensore
 */

public class DataManager implements SensorEventListener {

    private SensorManager manager;
    private Sensor accelerometer;
    private Context context;
    private AccelerometerDataEventListener accelerometerDataEventListener;
    private DataProcessor accelerometerDataProcessor;
    private static DataManager dataManager = null;

    /*
        Costruttore singleton
     */
    private DataManager(Context context) {
        this.context = context;
        // impostazioni per il sensore
        manager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(TYPE_ACCELEROMETER);
        manager.registerListener(this, accelerometer, SENSOR_DELAY_NORMAL);
        accelerometerDataProcessor = new DataProcessor();
    }

    /*
        Metodo per restituire l'unica instanza di DataManager se è già stata creata una volta
        oppure la crea.
     */
    public static DataManager getInstance(Context context) {
        if(dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==accelerometer.getType()) {       //Se gli eventi sono dell'accelerometro
            double x = event.values[0];
            double z = event.values[2];
            double gravity = 0.0;

            // Misuro la accelerazione di gravità che viene misurata dal sensore
            if ( x == 0 && z == 0) {
                gravity = event.values[1];
            }

            double y = getOrdinateValue(gravity, event.values[1]);
            AccelerometerDataEvent dataEvent = accelerometerDataProcessor.calculateData(x, y, z);
            accelerometerDataEventListener.onDataChanged(dataEvent);
        }
    }

    /**
     * Metodo per eliminare la componente di gravità da y.
     * @param gravity   valore dell'accelerazione di gravità
     * @param y         valore di accelerazione misurata al momento dell'evento
     * @return          valore dell'accelerazione senza l'accelerazione di gravità. Pronto per i calcoli
     */
    private double getOrdinateValue(double gravity, double y) {
        return (y - gravity);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    /*
        Metodo per registrare il listener
     */
    public void registerListener(AccelerometerDataEventListener accelerometerDataEventListener) {
        this.accelerometerDataEventListener = accelerometerDataEventListener;
    }
}
