package it.giuliatesta.udrive.dataProcessing;

import android.util.Log;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
import it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;
import it.giuliatesta.udrive.accelerometer.VerticalMotion;

import static it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent.createBothEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createBackwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createForwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createLeftEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createPotholeEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createRightEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createRoadBumpEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createStopEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.endOfLeftTurnEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.endOfPotholeEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.endOfRightTurnEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.endOfRoadBumpEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.isABackwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.isAForwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.startOfLeftTurnEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.startOfPotholeEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.startOfRightTurnEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.startOfRoadBumpEvent;
import static it.giuliatesta.udrive.dataProcessing.CalculatorHelper.getAccelerationVector;
import static it.giuliatesta.udrive.dataProcessing.CalculatorHelper.getDirection;
import static it.giuliatesta.udrive.dataProcessing.CalculatorHelper.getPercentage;
import static it.giuliatesta.udrive.dataProcessing.CalculatorHelper.getVerticalMotion;
import static it.giuliatesta.udrive.dataProcessing.CalculatorHelper.getVerticalMotionPercentage;
import static it.giuliatesta.udrive.dataProcessing.DataProcessor.AnalyzeResult.NEED_OTHER_EVENTS;
import static it.giuliatesta.udrive.dataProcessing.DataProcessor.AnalyzeResult.PROCESSED;

/**
    Classe che si occupa di elaborare i dati del sensore e generare i dati usati dalla UI
    x = indica destra sinistra
    y = indica sopra sotto
    z = indica avanti indietro
 */
public class DataProcessor {

    private final ArrayList<AccelerometerDataEventListener> eventListeners = new ArrayList<>();
    public enum AnalyzeResult { PROCESSED, NEED_OTHER_EVENTS }
    private boolean leftTurn = false, rightTurn = false, pothole = false, roadBump = false;

    /**
        Produce il dataEvent chiamando tutti i metodi che permettono di creare tutte le informazioni
     @return evento legato alla modifica valori rilevati dall'accelerometro
      * @param x coordinata x dell'accelerazione
     * @param y coordinata y dell'accelerazione
     * @param z coordinata z dell'accelerazione
     */
    public AccelerometerDataEvent calculateData(float x, float y, float z) {
        // Calcola il vettore accelerazione
        double vector = getAccelerationVector(x, y, z);

        // Calcola la direzione
        Direction direction = getDirection(x, z);

        // Calcola la percentuale della direzione
        int directionPercentage = getPercentage(vector);

        // Calcola il movimento vertical
        VerticalMotion verticalMotion = getVerticalMotion(y);

        // Calcola la percentuale legata al movimento verticale
        int verticalMotionPercentage = getVerticalMotionPercentage(verticalMotion, y);

        // Genera l'evento
        return createBothEvent(direction, directionPercentage, verticalMotion, verticalMotionPercentage);
    }

    /**
     * Metodo per analizzare i dati in arrivo dall'accelerometro, precedentemente filtrati per avere valori iniziali del tipo (0.0, 0.0, 0.0)
     * @param coordinatesDataEventArrayList     lista dei valori grezzi
     * @return  PROCESSED se è riconosciuto un pattern di valori;
     *          NEED_OTHER_EVENTS se non vengono riconosciuti o se è riconosciuto il pattern per la fine di una curva o di un movimento verticale
     */
    AnalyzeResult analyze(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList = generateAccelerometerEvents(coordinatesDataEventArrayList);

        if (isAForwardEvent(coordinatesDataEventArrayList)) {
            AccelerometerDataEvent straightForwardEvent = createForwardEvent(accelerometerDataEventArrayList);
            Log.d("DataProcessor", "analyze: FORWARD" );
            notifyListener(straightForwardEvent);
            return PROCESSED;
        }
        else if (isABackwardEvent(coordinatesDataEventArrayList)) {
            AccelerometerDataEvent backwardEvent = createBackwardEvent(accelerometerDataEventArrayList);
            notifyListener(backwardEvent);
            Log.d("DataProcessor", "analyze: BACKWARD" );
            return PROCESSED;
        }
        else if (startOfLeftTurnEvent(coordinatesDataEventArrayList)) {
            if(!rightTurn) {
                AccelerometerDataEvent leftTurnEvent = createLeftEvent(accelerometerDataEventArrayList);
                notifyListener(leftTurnEvent);
                Log.d("DataProcessor", "analyze: LEFT");
                leftTurn = true;
            }
            return PROCESSED;
        }
        else if (endOfLeftTurnEvent(coordinatesDataEventArrayList) && leftTurn) {
            Log.d("DataProcessor", "analyze: END OF LEFT" );
            leftTurn = false;
            return NEED_OTHER_EVENTS;
        }
        else if (startOfRightTurnEvent(coordinatesDataEventArrayList)) {
            if(!leftTurn) {
                AccelerometerDataEvent rightTurnEvent = createRightEvent(accelerometerDataEventArrayList);
                notifyListener(rightTurnEvent);
                Log.d("DataProcessor", "analyze: RIGHT" );
                rightTurn = true;
            }
            return PROCESSED;
        }
        else if(endOfRightTurnEvent(coordinatesDataEventArrayList) && rightTurn) {
            Log.d("DataProcessor", "analyze: END OF RIGHT" );
            rightTurn = false;
            return NEED_OTHER_EVENTS;
        }
        else if (startOfRoadBumpEvent(coordinatesDataEventArrayList)) {
            if(!pothole) {
                AccelerometerDataEvent roadBumpEvent = createRoadBumpEvent(accelerometerDataEventArrayList);
                notifyListener(roadBumpEvent);
                Log.d("DataProcessor", "analyze: ROADBUMP" );
                roadBump = true;
            }
            return PROCESSED;
        }
        else if (endOfRoadBumpEvent(coordinatesDataEventArrayList) && roadBump) {
            Log.d("DataProcessor", "analyze: END OF ROADBUMP" );
            roadBump = false;
            return NEED_OTHER_EVENTS;
        }
        else if (startOfPotholeEvent(coordinatesDataEventArrayList)) {
            if(!roadBump) {
                AccelerometerDataEvent potholeEvent = createPotholeEvent(accelerometerDataEventArrayList);
                notifyListener(potholeEvent);
                Log.d("DataProcessor", "analyze: POTHOLE" );
                pothole = true;
            }
            return PROCESSED;
        }
        else if (endOfPotholeEvent(coordinatesDataEventArrayList) && pothole) {
            Log.d("DataProcessor", "analyze: END OF POTHOLE" );
            pothole = false;
            return NEED_OTHER_EVENTS;
        }
        AccelerometerDataEvent stopEvent = createStopEvent();
        notifyListener(stopEvent);
        Log.d("DataProcessor", "analyze: STOPEVENT");
        return NEED_OTHER_EVENTS;
    }

    /**
     * Metodo per notificare tutti i listener
     * @param event evento da notificare
     */
    private void notifyListener(AccelerometerDataEvent event) {
        for(AccelerometerDataEventListener listener : eventListeners) {
            listener.onDataChanged(event);
        }
    }

    /**
     * Crea una lista di AccelerometerDataEvent a partire da SensorEvent
     * @param coordinatesDataEventArrayList lista di SensorEvent
     * @return lista di AccelerometerDataEvent
     */
    private ArrayList<AccelerometerDataEvent> generateAccelerometerEvents(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList = new ArrayList<>();
        for(CoordinatesDataEvent event : coordinatesDataEventArrayList) {
            AccelerometerDataEvent accelerometerDataEvent = calculateData(event.getX(), event.getY(), event.getZ());
            accelerometerDataEventArrayList.add(0, accelerometerDataEvent);
        }
        return accelerometerDataEventArrayList;
    }

    /**
     * Metodo per registrare il listener.
     * @param accelerometerDataEventListener    listener da registrare
     */
    void registerListener(AccelerometerDataEventListener accelerometerDataEventListener) {
        eventListeners.add(accelerometerDataEventListener);
    }

}
