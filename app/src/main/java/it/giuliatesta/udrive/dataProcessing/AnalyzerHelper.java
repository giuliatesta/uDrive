package it.giuliatesta.udrive.dataProcessing;

import java.util.ArrayList;

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
import static java.lang.Math.abs;

public class AnalyzerHelper {
    /**
     * Controlla il tipo di evento che è appena arrivato dal sensore
     * @param coordinatesDataEventArrayList   lista degli eventi
     * @return  true se l'ultimo è FORWARD
     */
    static boolean isAForwardEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        float currentZ = currentEvent.getZ();
        float previousZ = previousEvent.getZ();
        float currentX = currentEvent.getX();
        float currentY = currentEvent.getY();

        return previousZ == 0.0 && currentZ > 0.0 && abs(currentX) < abs(currentZ) && abs(currentY) < abs(currentZ);
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
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        float currentZ = currentEvent.getZ();
        float previousZ = previousEvent.getZ();
        float currentX = currentEvent.getX();
        float currentY = currentEvent.getY();
        return previousZ == 0.0 && currentZ < 0.0 && abs(currentX) < abs(currentZ) && abs(currentY) < abs(currentZ);
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
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        float currentY = currentEvent.getY();
        float currentZ = currentEvent.getZ();
        return previousX == 0.0 && currentX < 0.0 && abs(currentY) < abs(currentX) && abs(currentZ) < abs(currentX);
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
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        return previousX > 0.0 && currentX == 0.0;
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

        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        float currentY = currentEvent.getY();
        float currentZ = currentEvent.getZ();
        return previousX == 0.0 && currentX > 0.0 && abs(currentY) < abs(currentX) && abs(currentZ) < abs(currentX);
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
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        return previousX < 0.0 && currentX == 0.0;
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
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        float currentX = currentEvent.getX();
        float currentZ = currentEvent.getZ();
        return previousY == 0.0 && currentY > 0.0 && abs(currentX) < abs(currentY) && abs(currentZ) < abs(currentY);
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
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        return previousY < 0.0 && currentY == 0.0;
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
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        float currentX = currentEvent.getX();
        float currentZ = currentEvent.getZ();
        return previousY == 0.0 && currentY < 0.0 && abs(currentX) < abs(currentY) && abs(currentZ) < abs(currentY);
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
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        return previousY > 0.0 && currentY == 0.0;
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
