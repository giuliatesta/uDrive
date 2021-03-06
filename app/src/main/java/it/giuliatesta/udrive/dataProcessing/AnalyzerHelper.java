package it.giuliatesta.udrive.dataProcessing;

import java.util.ArrayList;
import java.util.List;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;
import it.giuliatesta.udrive.accelerometer.VerticalMotion;

import static it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent.createDirectionEvent;
import static it.giuliatesta.udrive.accelerometer.Direction.BACKWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.FORWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.LEFT;
import static it.giuliatesta.udrive.accelerometer.Direction.RIGHT;
import static it.giuliatesta.udrive.accelerometer.EventType.VERTICAL_MOTION_EVENT;
import static it.giuliatesta.udrive.dataProcessing.CalculatorHelper.getAccelerationVector;
import static it.giuliatesta.udrive.dataProcessing.CalculatorHelper.getVerticalMotion;
import static it.giuliatesta.udrive.dataProcessing.Configuration.INSTANCE;
import static java.lang.Math.abs;

/**
 * Classe di supporto per l'analisi degli eventi che arrivano dall'accelerometro
 */
class AnalyzerHelper {

    private static double minX = INSTANCE.getMinValueX();
    private static double minY = INSTANCE.getMinValueY();
    private static double minZ = INSTANCE.getMinValueZ();

    /**
     * Classe per la il recupero delle coordinate degli eventi nella lista,
     * in particolare dell'ultimo e del penultimo
     */
    private static class EventsUnderObservation {
        private final List<CoordinatesDataEvent> eventsList;

        /**
         * Costruttore
         * @param eventsList    lista di eventi
         */
        private EventsUnderObservation(List<CoordinatesDataEvent> eventsList) {
            this.eventsList = eventsList;
        }

        /**
         * Restituisce il valore della coordinata Z del penultimo evento
         * @return  coordinata Z
         */
        double previousZ() {
            return eventsList.get(1).getZ();
        }

        /**
         * Restituisce il valore della coordinata Z dell'ultimo evento
         * @return  coordinata Z
         */
        double currentZ() {
            return eventsList.get(0).getZ();
        }

        /**
         * Restituisce il valore della coordinata X dell'ultimo evento
         * @return  coordinata X
         */
        double currentX() {
            return eventsList.get(0).getX();
        }

        /**
         * Restituisce il valore della coordinata Y dell'ultimo evento
         * @return  coordinata Y
         */
        double currentY() {
            return eventsList.get(0).getY();
        }

        /**
         * Restituisce il valore della coordinata X del penultimo evento
         * @return  coordinata X
         */
        double previousX() {
            return eventsList.get(1).getX();
        }

        /**
         * Restituisce il valore della coordinata Y del penultimo evento
         * @return  coordinata Y
         */
        double previousY() {
            return eventsList.get(1).getY();
        }
    }

    /**
     * Controlla il tipo di evento che è appena arrivato dal sensore
     * @param coordinatesDataEventArrayList   lista degli eventi
     * @return  true se l'ultimo è FORWARD
     */
    static boolean isAForwardEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (abs(events.previousZ()) < minZ) && (events.currentZ() > minZ)
                && (abs(events.currentX()) < abs(events.currentZ()))
                && (abs(events.currentY()) < abs(events.currentZ()));
    }

    /**
     * Controlla il tipo di evento che è appena arrivato dal sensore
     * @param coordinatesDataEventArrayList   lista degli eventi
     * @return  true se l'ultimo è BACKARD
     */
    static boolean isABackwardEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        // Se la lista è vuota
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (abs(events.previousZ()) < minZ) && (events.currentZ() < -minZ)
                && (abs(events.currentX()) < abs(events.currentZ()))
                && (abs(events.currentY()) < abs(events.currentZ()));
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è l'inizio di una curva verso sinistra
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è l'inizio di una curva a sinistra
     */
    static boolean startOfLeftTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (abs(events.previousX()) < minX) && (events.currentX() < -minX)
                && (abs(events.currentY()) < abs(events.currentX()))
                && (abs(events.currentZ()) < abs(events.currentX()));
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è la fine di una curva verso sinistra
     * @param coordinatesDataEventArrayList     lista degli eventi grezzi
     * @return  true se è la fine di una curva a sinistra
     */
    static boolean endOfLeftTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if (coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (events.previousX() > minX) && (events.currentX() < minX);
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è l'inizio di una curva verso destra
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è l'inizio di una curva a destra
     */
    static boolean startOfRightTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (abs(events.previousX()) < minX) && (events.currentX() > minX)
                && (abs(events.currentY()) < abs(events.currentX()))
                && (abs(events.currentZ()) < abs(events.currentX()));
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è la fine di una curva verso destra
     * @param coordinatesDataEventArrayList     lista degli eventi grezzi
     * @return  true se è la fine di una curva a destra
     */
    static boolean endOfRightTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (events.previousX() < -minX) && (abs(events.currentX()) < minX);
    }

    /**
     * Controlla se il tipo di evento che è arrivato dal sensore è un movimento verticale (o dosso o buca)
     * @param coordinatesDataEventArrayList     lista degli eventi grezzi
     * @return  true se è un movimento verticale
     */
    static boolean isARoadBumpEventOrPotholeEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.isEmpty()) {
            return false;
        }
        CoordinatesDataEvent maxEvent = calculateEventWithMaximumIntensityForY(coordinatesDataEventArrayList);
        return (abs(maxEvent.getY()) > minY);

    }

    /**
     * Calcola nell'elenco di eventi grezzi quale è quello con intensità maggiore sulla coordinata Y
     * @param coordinatesDataEventArrayList     lista degli eventi grezzi
     * @return      evento grezzo con intensità massima sulla Y
     */
    static CoordinatesDataEvent calculateEventWithMaximumIntensityForY(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        CoordinatesDataEvent max = coordinatesDataEventArrayList.get(0);
        for(CoordinatesDataEvent event : coordinatesDataEventArrayList){
            if(abs(event.getY()) > abs(max.getY())) {
                max = event;
            }
        }
        return max;
    }

    /**
     * Cerca il valore della percentuale ottenuta dall'evento in base ad una direzione particolare
     * @param direction     direzione da cercare nella lista
     * @param accelerometerDataEventArrayList   lista di eventi
     * @return  il punteggio ottenuto da quella particolare direzione
     */
    private static int findDirectionPercentage(Direction direction, ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        int percentage = 0;
        for(AccelerometerDataEvent event : accelerometerDataEventArrayList) {
            if(event.getType() != VERTICAL_MOTION_EVENT && event.getDirection() == direction) {
                percentage = event.getDirectionPercentage();
            }
        }
        return percentage;
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione FORWARD di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista di eventi considerata
     * @return evento FORWARD
     */
    static AccelerometerDataEvent createForwardEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = FORWARD;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione BACKWARD di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista di eventi considerata
     * @return evento BACKWARD
     */
    static AccelerometerDataEvent createBackwardEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = BACKWARD;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione LEFT di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento LEFT
     */
    static AccelerometerDataEvent createLeftEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = LEFT;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione RIGHT di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento RIGHT
     */
    static AccelerometerDataEvent createRightEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = RIGHT;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento di tipo VERTICAL_MOTION, calcolando quale movimento sia in base al valore di Y
     * @param maximumIntensityEvent     evento grezzo
     * @return      evento elaborato finale
     */
    static AccelerometerDataEvent createARoadBumpOrPotholeEvent(CoordinatesDataEvent maximumIntensityEvent) {
        VerticalMotion verticalMotion = getVerticalMotion(maximumIntensityEvent.getY());
        int percentage = CalculatorHelper.getPercentage(getAccelerationVector(maximumIntensityEvent.getX(), maximumIntensityEvent.getY(), maximumIntensityEvent.getZ()));
        return AccelerometerDataEvent.createVerticalMotionEvent(verticalMotion, percentage);
    }

    /**
     * Crea un evento dell'accelerometro che rappresenta nessun movimento
     * @return evento di stop
     */
    static AccelerometerDataEvent createStopEvent() {
        AccelerometerDataEvent stopEvent = new AccelerometerDataEvent();
        stopEvent.setAStopEvent(true);
        return stopEvent;
    }

}
