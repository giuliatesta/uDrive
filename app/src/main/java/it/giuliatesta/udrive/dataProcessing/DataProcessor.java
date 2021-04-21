package it.giuliatesta.udrive.dataProcessing;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
import it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;
import it.giuliatesta.udrive.accelerometer.VerticalMotion;

import static it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent.createBothEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.calculateEventWithMaximumIntensityForY;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createARoadBumpOrPotholeEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createBackwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createForwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createLeftEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createRightEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.createStopEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.endOfLeftTurnEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.endOfRightTurnEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.isABackwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.isAForwardEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.isARoadBumpEventOrPotholeEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.startOfLeftTurnEvent;
import static it.giuliatesta.udrive.dataProcessing.AnalyzerHelper.startOfRightTurnEvent;
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
class DataProcessor {

    private final ArrayList<AccelerometerDataEventListener> eventListeners = new ArrayList<>();
    public enum AnalyzeResult { PROCESSED, NEED_OTHER_EVENTS }
    private boolean leftTurn = false, rightTurn = false;
    private long currentWindowStartTime = 0L;
    private static final long MAX_WINDOW = 1_500_000_000L;      // Dimensione massima della finestra

    /**
     Produce il dataEvent chiamando tutti i metodi che permettono di creare tutte le informazioni
     @return evento legato alla modifica valori rilevati dall'accelerometro
      * @param x coordinata x dell'accelerazione
     * @param y coordinata y dell'accelerazione
     * @param z coordinata z dell'accelerazione
     */
    private AccelerometerDataEvent calculateData(float x, float y, float z) {
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

        // Inizializzo la finestra al valore in nanosecondi del primo evento arrivato della lista
        if(currentWindowStartTime == 0L) {
            currentWindowStartTime = coordinatesDataEventArrayList.get(0).getEventTime();
        }

        // Se l'evento è un evento di tipo direzione FORWARD
        if (isAForwardEvent(coordinatesDataEventArrayList)) {
            AccelerometerDataEvent straightForwardEvent = createForwardEvent(accelerometerDataEventArrayList);
            notifyListener(straightForwardEvent);
            return PROCESSED;
        }
        // Se l'evento è un evento di tipo direzione BACKWARD
        else if (isABackwardEvent(coordinatesDataEventArrayList)) {
            AccelerometerDataEvent backwardEvent = createBackwardEvent(accelerometerDataEventArrayList);
            notifyListener(backwardEvent);
            return PROCESSED;
        }

        // Se l'evento è un evento di tipo direzione LEFT
        else if (startOfLeftTurnEvent(coordinatesDataEventArrayList)) {
            if(!rightTurn) {
                AccelerometerDataEvent leftTurnEvent = createLeftEvent(accelerometerDataEventArrayList);
                notifyListener(leftTurnEvent);
                leftTurn = true;
            }
            return PROCESSED;
        }
        // Se l'evento di tipo direzione LEFT è terminato
        else if (endOfLeftTurnEvent(coordinatesDataEventArrayList) && leftTurn) {
            leftTurn = false;
            return NEED_OTHER_EVENTS;
        }
        // Se l'evento è un evento di tipo direzione RIGHT
        else if (startOfRightTurnEvent(coordinatesDataEventArrayList)) {
            if(!leftTurn) {
                AccelerometerDataEvent rightTurnEvent = createRightEvent(accelerometerDataEventArrayList);
                notifyListener(rightTurnEvent);
                rightTurn = true;
            }
            return PROCESSED;
        }
        // Se l'evento di tipo direzione RIGHT è terminato
        else if(endOfRightTurnEvent(coordinatesDataEventArrayList) && rightTurn) {
            rightTurn = false;
            return NEED_OTHER_EVENTS;
        }
        // Se la finestra di raccolta degli eventi è conclusa e individuo un possibile dosso/buca
        else if(isCurrentWindowClosed(coordinatesDataEventArrayList)) {
            if(isARoadBumpEventOrPotholeEvent(coordinatesDataEventArrayList)) {
                // Calcolo l'evento con intensità massima
                CoordinatesDataEvent maximumIntensityEvent = calculateEventWithMaximumIntensityForY(coordinatesDataEventArrayList);
                // Creo l'accelerometerDataEvent corrispondente a partire dall'evento con intensità massima
                AccelerometerDataEvent event = createARoadBumpOrPotholeEvent(maximumIntensityEvent);
                // Notifico il listener
                notifyListener(event);
            }

            // Inizializzo nuovamente la finestra
            currentWindowStartTime += MAX_WINDOW;
            return PROCESSED;
        }
        // Se l'evento è un evento di tipo STOP_EVENT
        AccelerometerDataEvent stopEvent = createStopEvent();
        notifyListener(stopEvent);
        return NEED_OTHER_EVENTS;
    }

    /**
     * Metodo per determinare se la finestra corrente è conclusa
     * @param coordinatesDataEventArrayList     lista degli eventi grezzi
     * @return  TRUE    se la finestra è di almeno 2s; FALSE altrimenti
     */
    private boolean isCurrentWindowClosed(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        return coordinatesDataEventArrayList.get(0).getEventTime() > (currentWindowStartTime + MAX_WINDOW);
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
            // Crea gli eventi elaborati
            AccelerometerDataEvent accelerometerDataEvent = calculateData(event.getX(), event.getY(), event.getZ());
            // Li aggiunge alla lista
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
