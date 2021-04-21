package it.giuliatesta.udrive.dataProcessing;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
import it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent;
import it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent.DeviceOrientation;
import it.giuliatesta.udrive.dataProcessing.DataProcessor.AnalyzeResult;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_LINEAR_ACCELERATION;
import static it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent.lowPassFiltering;
import static it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent.setOrientation;
import static it.giuliatesta.udrive.dataProcessing.DataProcessor.AnalyzeResult.PROCESSED;

/**
 Classe per la gestione degli eventi causati dalla modifica dei valori del sensore
 */

public class DataManager implements SensorEventListener {

    private Sensor accelerometer;
    private SensorManager sensorManager;
    private final Context context;
    private DataProcessor accelerometerDataProcessor;
    private ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList;
    private static DataManager dataManager = null;
    private final StorageListener storageListener;
    private static DeviceOrientation deviceOrientation = null;

    /**
        Costruttore singleton
     */
    private DataManager(Context context) {
        this.context = context;

        sensorSettings();

        // Registrazione del listener
        storageListener = new StorageListener();
        this.registerListener(storageListener);
    }

    /**
     * Metodo per le impostazioni del sensore: scelgo il tipo di sensore, registro
     * il suo listener e credo un nuovo DataProcessor per manipolare i dati
     */
    private void sensorSettings() {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(TYPE_LINEAR_ACCELERATION);
        accelerometerDataProcessor = new DataProcessor();
        coordinatesDataEventArrayList = new ArrayList<>();
    }

    /**
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
            analyzeSensorEvent(event);
        }
    }

    /**
     * Metodo per analizzare l'evento arrivato: viene filtrato, viene scelto l'orientamento corretto ed elaborato
     * @param event evento del sensore in arrivo
     */
    private void analyzeSensorEvent(SensorEvent event) {
        float[] accelerometerValues = new float[3];
        // applico il filtro passa basso alle coordinate dell'evento
        accelerometerValues = lowPassFiltering(event.values.clone(), accelerometerValues);

        // applico l'orientamento richiesto
        accelerometerValues = setOrientation(accelerometerValues, deviceOrientation);

        // creo un evento grezzo con le tre coordinate e il tempo in nanosecondi di arrivo
        CoordinatesDataEvent coordinatesDataEvent = new CoordinatesDataEvent(accelerometerValues[0], accelerometerValues[1], accelerometerValues[2]);

        // aggiunto l'evento grezzo alla lista di eventi
        coordinatesDataEventArrayList.add(0, coordinatesDataEvent);     // L'ultimo arrivato è il primo della lista

        // Passo la lista di eventi grezzi all'elaborazione
        AnalyzeResult result = accelerometerDataProcessor.analyze(coordinatesDataEventArrayList);

        //Se l'elaborazione è completata, scrivo i dati sul file e svuoto la lista
        if (result == PROCESSED) {
            storageListener.writeCoordinates(coordinatesDataEventArrayList);
            coordinatesDataEventArrayList.clear();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
        Metodo per registrare il listener
     */
    public void registerListener(AccelerometerDataEventListener accelerometerDataEventListener) {
        accelerometerDataProcessor.registerListener(accelerometerDataEventListener);
    }

    /**
     * Metodo get che restituisce il sensor manager
     * @return  manager dell'accelerometro
     */
    public SensorManager getSensorManager() {
        return sensorManager;
    }

    /**
     * Metodo get che resistuisce il listener legato allo storage
     * @return  storageListener
     */
    public StorageListener getStorageListener() {
        return storageListener;
    }

    /**
     * Metodo get che restituisce l'accelerometro
     * @return  sensore accelerometro
     */
    public Sensor getAccelerometer() {
        return accelerometer;
    }

    /**
     * Metodo set per impostare l'orientamento scelto dall'utente
     * @param deviceOrientation     orientamento scelto
     */
    public static void setDeviceOrientation(DeviceOrientation deviceOrientation) {
        DataManager.deviceOrientation = deviceOrientation;
    }
}
