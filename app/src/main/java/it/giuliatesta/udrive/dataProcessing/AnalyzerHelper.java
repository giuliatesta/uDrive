package it.giuliatesta.udrive.dataProcessing;

import java.util.ArrayList;
import java.util.List;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;
import it.giuliatesta.udrive.accelerometer.VerticalMotion;

import static it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent.createDirectionEvent;
import static it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent.createVerticalMotionEvent;
import static it.giuliatesta.udrive.accelerometer.Direction.BACKWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.FORWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.LEFT;
import static it.giuliatesta.udrive.accelerometer.Direction.RIGHT;
import static it.giuliatesta.udrive.accelerometer.EventType.DIRECTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.EventType.VERTICAL_MOTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.POTHOLE;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.ROADBUMP;
import static it.giuliatesta.udrive.dataProcessing.Constants.MinValue;
import static java.lang.Math.abs;

/**
 * Classe di supporto per l'analisi degli eventi che arrivano dall'accelerometro
 */
class AnalyzerHelper {

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
        return (abs(events.previousZ()) < MinValue) && (events.currentZ() > 0.0)
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
        return (abs(events.previousZ()) < MinValue) && (events.currentZ() < 0.0)
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
        return (abs(events.previousX()) < MinValue) && (events.currentX() < 0.0)
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
        return (events.previousX() > 0.0) && (events.currentX() == 0.0);
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
        return (abs(events.previousX()) < MinValue) && (events.currentX() > 0.0)
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
        return (events.previousX() < 0.0) && (abs(events.currentX()) < MinValue);
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è l'inizio di un dosso
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è l'inizio di un dosso
     */
    static boolean startOfRoadBumpEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (abs(events.previousY()) < MinValue) && (events.currentY() > 0.0)
                && (abs(events.currentX()) < abs(events.currentY())) && (abs(events.currentZ()) < abs(events.currentY()));
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è la fine di un dosso
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è la fine di un dosso
     */
    static boolean endOfRoadBumpEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (events.previousY() < 0.0) && (abs(events.currentY()) < MinValue);
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è l'inizio di un buco
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è l'inizio di un buco
     */
    static boolean startOfPotholeEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (abs(events.previousY()) < MinValue) && (events.currentY() < 0.0)
                && (abs(events.currentX()) < abs(events.currentY()))
                && (abs(events.currentZ()) < abs(events.currentY()));
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è la fine di un buco
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è la fine di un buco
     */
    static boolean endOfPotholeEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        EventsUnderObservation events = new EventsUnderObservation(coordinatesDataEventArrayList);
        return (events.previousY() > 0.0) && (abs(events.currentY()) < MinValue);
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
     * Crea un evento dell'accelerometro che ha un movimento verticale di tipo ROADBUMP
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento ROADBUMP
     */
    static AccelerometerDataEvent createRoadBumpEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        VerticalMotion verticalMotion = ROADBUMP;
        int verticalMotionPercentage = findVerticalMotionPercentage(verticalMotion, accelerometerDataEventArrayList);
        return createVerticalMotionEvent(verticalMotion, verticalMotionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha un movimento verticale di tipo POTHOLE
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento POTHOLE
     */
    static AccelerometerDataEvent createPotholeEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        VerticalMotion verticalMotion = POTHOLE;
        int verticalMotionPercentage = findVerticalMotionPercentage(verticalMotion, accelerometerDataEventArrayList);
        return createVerticalMotionEvent(verticalMotion, verticalMotionPercentage);
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

    /**
     *Cerca il valore della percentuale ottenuta dall'evento in base ad un movimento verticale particolare
     * @param verticalMotion    movemento verticale da cercare nella lista di eventi
     * @param accelerometerDataEventArrayList   lista di eventi
     * @return  il punteggio ottenuto da quel particolare movimento verticale
     */
    private static int findVerticalMotionPercentage(VerticalMotion verticalMotion, ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        int percentage = 0;
        for(AccelerometerDataEvent event : accelerometerDataEventArrayList) {
            if(event.getType() != DIRECTION_EVENT && event.getVerticalMotion() == verticalMotion) {
                percentage = event.getVerticalMotionPercentage();
            }
        }
        return percentage;
    }

}
