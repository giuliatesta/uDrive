package it.giuliatesta.udrive;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

import it.giuliatesta.udrive.DataProcessor.AnalyzeResult;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;
import static it.giuliatesta.udrive.DataProcessor.AnalyzeResult.PROCESSED;

/**
 Classe per la gestione degli eventi causati dalla modifica dei valori del sensore
 */

public class DataManager implements SensorEventListener {

    private Sensor accelerometer;
    private Context context;
    private AccelerometerDataEventListener accelerometerDataEventListener;
    private DataProcessor accelerometerDataProcessor;
    private ArrayList<EventData> eventDataArrayList;
    private static DataManager dataManager = null;

    // Coordinate precedenti dell'accelerazione
    private double historyX = 0.0;
    private double historyY = 0.0;
    private double historyZ = 0.0;

    /**
        Costruttore singleton
     */
    private DataManager(Context context) {
        this.context = context;

        sensorSettings();
    }

    /**
     * Metodo per le impostazioni del sensore: scelgo il tipo di sensore, registro
     * il suo listener e credo un nuovo DataProcessor per manipolare i dati
     */
    private void sensorSettings() {
        SensorManager manager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(TYPE_ACCELEROMETER);
        manager.registerListener(this, accelerometer, SENSOR_DELAY_NORMAL);
        accelerometerDataProcessor = new DataProcessor();
        eventDataArrayList = new ArrayList<EventData>();
    }

    /**
        Metodo per restituire l'unica instanza di DataManager se è già stata creata una volta
        oppure la crea.
     */
    static DataManager getInstance(Context context) {
        if(dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==accelerometer.getType()) {       //Se gli eventi sono dell'accelerometro
            Log.d("DataManager", "onSensorChanged: " + event);
            float x = event.values[0];
            float y = getYValue(event.values[1]);
            float z = getZValue(event.values[2]);

            Log.d("DataManager", "onSensorChanged : " + x + "   " + y + "    "+ z);
            // Notifico il listener dell'accelerometro con i dati calcolati solo quando la variazione è significativa
            eventDataArrayList.add(0, new EventData(x, y, z));
            AnalyzeResult result = accelerometerDataProcessor.analyze(eventDataArrayList);
            if (result == PROCESSED) {
                eventDataArrayList.clear();
            }
        }
    }

    /**
     * Metodo per eliminare la componente di gravità da y.
     * @param y         valore di accelerazione misurata al momento dell'evento
     * @return          valore dell'accelerazione senza l'accelerazione di gravità. Pronto per i calcoli
     */
    private float getYValue(float y) {
        return (y - 9.77F);
    }

    private float getZValue(float z) {
        return (z - 0.81F);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
        Metodo per registrare il listener
     */
    public void registerListener(AccelerometerDataEventListener accelerometerDataEventListener) {
        this.accelerometerDataEventListener = accelerometerDataEventListener;
        accelerometerDataProcessor.registerListener(accelerometerDataEventListener);
    }
}
