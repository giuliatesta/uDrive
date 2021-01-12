package it.giuliatesta.udrive;

import android.util.Log;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
import it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;
import it.giuliatesta.udrive.accelerometer.VerticalMotion;

import static it.giuliatesta.udrive.Constants.MaxValue;
import static it.giuliatesta.udrive.Constants.MinValue;
import static it.giuliatesta.udrive.Constants.fourtyFiveDegree;
import static it.giuliatesta.udrive.Constants.oneHundredThirtyFive;
import static it.giuliatesta.udrive.Constants.threeHundredFifteen;
import static it.giuliatesta.udrive.Constants.threeHundredSixty;
import static it.giuliatesta.udrive.Constants.twoHundrenTwentyFive;
import static it.giuliatesta.udrive.Constants.zeroDegree;
import static it.giuliatesta.udrive.DataProcessor.AnalyzeResult.NEED_OTHER_EVENTS;
import static it.giuliatesta.udrive.DataProcessor.AnalyzeResult.PROCESSED;
import static it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent.createDirectionEvent;
import static it.giuliatesta.udrive.accelerometer.Direction.BACKWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.FORWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.LEFT;
import static it.giuliatesta.udrive.accelerometer.Direction.RIGHT;
import static it.giuliatesta.udrive.accelerometer.EventType.DIRECTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.EventType.VERTICAL_MOTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.NONE;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.POTHOLE;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.ROADBUMP;
import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
/**
    Classe che si occupa di elaborare i dati del sensore e generare i dati usati dalla UI
    x = indica destra sinistra
    y = indica sopra sotto
    z = indica avanti indietro
 */
class DataProcessor {

    private AccelerometerDataEventListener accelerometerDataEventListener;
    public enum AnalyzeResult { PROCESSED, NEED_OTHER_EVENTS }
    private boolean leftTurn = false, rightTurn = false, pothole = false, roadBump = false;

    /**
        Calcola il modulo del vettore accelerazione
     */
    private double getAccelerationVector(double x, double y, double z) {

        // Calcola la radice quadrata della somma dei quadrati delle coordinate
        double absVector = sqrt(pow(x,2) + pow(y - 9.81,2) + pow(z,2));
       if(z > 0) {
            // Se la componente z è positiva significa che sta accelerando nel senso di marcia --> accelera
            return absVector;
        } else {
            // Altrimenti sta decelerando
            return (-absVector);
        }
    }

    /**
     Calcola la posizione dell'angolo nei 4 quadranti
     */
    private double getPositionOfAlpha(double x, double z) {
        double alpha = Math.toDegrees(atan2(z, x));     //Angolo in gradi
        return ((int) (alpha+360))%360;
    }

    /**
        Calcola la direzione
     @param x  coordinata x dell'accelerazione
     @param z  coordinata z dell'accelerazione
     */
    private Direction getDirection(double x, double z) {
        Direction direction = Direction.NONE;
        double alpha = getPositionOfAlpha(x, z);

        if (alpha >= fourtyFiveDegree && alpha <= oneHundredThirtyFive) {
            // Se l'angolo è compreso tra 45 e 135
            direction = FORWARD;
        } else if (alpha >= twoHundrenTwentyFive && alpha <= threeHundredFifteen) {
            // Se l'angolo è compreso tra 225 e 315
            direction= BACKWARD;
        } else if ((alpha > threeHundredFifteen && alpha <= threeHundredSixty) || (alpha >= zeroDegree && alpha < fourtyFiveDegree)) {
            // Se l'angolo è compreso tra -45 e 45
            direction = RIGHT;
        } else if (alpha > oneHundredThirtyFive && alpha < twoHundrenTwentyFive) {
            direction = LEFT;
        }
        return direction;
    }

    /**
        Se il vettore supera il valore massimo oppure è minore del valore minino il punteggio è zero;
        mentre se rientra nel range viene calcolata una percentuale particolare
        @param vector vettore accelerazione
        @return percentuale ottenuta
     */
    private int getPercentage(double vector) {
            if(vector > MaxValue) {
                // Se si trova al di sopra dell'intervallo significa che si ha il minimo comfort
                return 0;
            } else if (vector< MinValue) {
                // Se si trova al di sotto dell'intervallo significa che si ha il massimo comfort
                return 100;
            } else {
                // Se si trova all'interno dell'intervallo, chiama il metodo per calcolare la percentuale
                return calculatePercentage(vector);
            }
    }

    /**
        Calcola una percentuale. Fa la proporzione x : 100 = vector : MaxValue
        @param vector vettore accelerazione
        @return percentuale
     */
    private int calculatePercentage(double vector) {
        return (int) ((vector*100)/MaxValue);
    }

    /**
     * Calcola il movimento verticale
     * @param y coomponente y del vettore accelerazione
     * @return corrispondente movimento verticale
     */
    private VerticalMotion getVerticalMotion(double y) {
        VerticalMotion verticalMotion = NONE;
        if (y > MinValue) {
            verticalMotion = ROADBUMP;
        } else if (y < -MinValue) {
            verticalMotion = POTHOLE;
        }
        return verticalMotion;
    }

    /**
     * Calcola la percentuale per il movimento verticale
     * @param verticalMotion tipo di movimento vertical
     * @param y valore di accelerazione
     * @return percentuale ottenuta
     */
    private int getVerticalMotionPercentage(VerticalMotion verticalMotion, double y) {
        if (verticalMotion != NONE) {
            return getPercentage(y);
        } else {
            return 0;
        }
    }

    /**
        Produce il dataEvent chiamando tutti i metodi che permettono di creare tutte le informazioni
        @param x coordinata x dell'accelerazione
        @param y coordinata y dell'accelerazione
        @param z coordinata z dell'accelerazione
        @return evento legato alla modifica valori rilevati dall'accelerometro
     */
    AccelerometerDataEvent calculateData(double x, double y, double z) {
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
        return new AccelerometerDataEvent(direction, directionPercentage, verticalMotion, verticalMotionPercentage, vector);
    }

    AnalyzeResult analyze(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList = generateAccelerometerEvents(coordinatesDataEventArrayList);
        if (isAForwardEvent(coordinatesDataEventArrayList)) {
            Log.d("DataProcessor", "analyze: FORWARD");
            AccelerometerDataEvent straightForwardEvent = createForwardEvent(accelerometerDataEventArrayList);
            notifyListener(straightForwardEvent);
            return PROCESSED;
        } else if(isABackwardEvent(coordinatesDataEventArrayList)) {
            Log.d("DataProcessor", "analyze: BACKWARD");
            AccelerometerDataEvent backwardEvent = createBackwardEvent(accelerometerDataEventArrayList);
            notifyListener(backwardEvent);
            return PROCESSED;
        }
        else if (startOfLeftTurnEvent(coordinatesDataEventArrayList)) {
            Log.d("DataProcessor", "analyze: LEFT");
            if(!rightTurn) {
                AccelerometerDataEvent leftTurnEvent = createLeftEvent(accelerometerDataEventArrayList);
                notifyListener(leftTurnEvent);
                leftTurn = true;
            }
            return PROCESSED;
        }
        else if(endOfLeftTurnEvent(coordinatesDataEventArrayList) && leftTurn) {
            Log.d("DataProcessor", "analyze: END OF LEFT");
            leftTurn = false;
            return NEED_OTHER_EVENTS;
        }
        else if(endOfRightTurnEvent(coordinatesDataEventArrayList) && rightTurn) {
            Log.d("DataProcessor", "analyze: END OF RIGHT");
            rightTurn = false;
            return NEED_OTHER_EVENTS;
        }
        else if (startOfRightTurnEvent(coordinatesDataEventArrayList)) {
            Log.d("DataProcessor", "analyze: RIGHT");
            if(!leftTurn) {
                AccelerometerDataEvent rightTurnEvent = createRightEvent(accelerometerDataEventArrayList);
                notifyListener(rightTurnEvent);
                rightTurn = true;
            }
            return PROCESSED;
        }
        else if (startOfRoadBumpEvent(coordinatesDataEventArrayList)) {
            Log.d("DataProcessor", "analyze: ROADBUMP");
            if(!pothole) {
                AccelerometerDataEvent roadBumpEvent = createRoadBumpEvent(accelerometerDataEventArrayList);
                notifyListener(roadBumpEvent);
                roadBump = true;
            }
            return PROCESSED;
        }
        else if(endOfRoadBumpEvent(coordinatesDataEventArrayList) && roadBump) {
            Log.d("DataProcessor", "analyze: END OF ROADBUMP");
            roadBump = false;
            return NEED_OTHER_EVENTS;
        }
        else if (startOfPotholeEvent(coordinatesDataEventArrayList)) {
            Log.d("DataProcessor", "analyze: POTHOLE");
            if(!roadBump) {
                AccelerometerDataEvent potholeEvent = createPotholeEvent(accelerometerDataEventArrayList);
                notifyListener(potholeEvent);
                pothole = true;
            }
            return PROCESSED;
        }
        else if(endOfPotholeEvent(coordinatesDataEventArrayList) && pothole) {
            Log.d("DataProcessor", "analyze: END OF POTHOLE");
            pothole = true;
            return NEED_OTHER_EVENTS;
        }
        Log.d("DataProcessor", "analyze: STOPEVENT");
        AccelerometerDataEvent stopEvent = new AccelerometerDataEvent();
        stopEvent.setAStopEvent(true);
        notifyListener(stopEvent);
        return NEED_OTHER_EVENTS;
    }

    private boolean endOfPotholeEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        return previousY > 0.0 && currentY == 0.0;
    }

    private boolean endOfRoadBumpEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        return previousY < 0.0 && currentY == 0.0;
    }

    private boolean endOfRightTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        return previousX < 0.0 && currentX == 0.0;
    }

    private boolean endOfLeftTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if (coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        return previousX > 0.0 && currentX == 0.0;
    }

    private void notifyListener(AccelerometerDataEvent event) {
        if (accelerometerDataEventListener != null) {
            accelerometerDataEventListener.onDataChanged(event);
        }
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione FORWARD di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista di eventi considerata
     * @return evento FORWARD
     */
    private AccelerometerDataEvent createForwardEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = FORWARD;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione BACKWARD di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista di eventi considerata
     * @return evento BACKWARD
     */
    private AccelerometerDataEvent createBackwardEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = BACKWARD;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione LEFT di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento LEFT
     */
    private AccelerometerDataEvent createLeftEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = LEFT;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha direzione RIGHT di tipo DIRECTION_EVENT
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento RIGHT
     */
    private AccelerometerDataEvent createRightEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = RIGHT;
        int directionPercentage = findDirectionPercentage(direction, accelerometerDataEventArrayList);
        return createDirectionEvent(direction, directionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha un movimento verticale di tipo ROADBUMP
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento ROADBUMP
     */
    private AccelerometerDataEvent createRoadBumpEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        VerticalMotion verticalMotion = ROADBUMP;
        int verticalMotionPercentage = findVerticalMotionPercentage(verticalMotion, accelerometerDataEventArrayList);
        return AccelerometerDataEvent.createVerticalMotionEvent(verticalMotion, verticalMotionPercentage);
    }

    /**
     * Crea un evento dell'accelerometro che ha un movimento verticale di tipo POTHOLE
     * @param accelerometerDataEventArrayList lista degli eventi considerati
     * @return evento POTHOLE
     */
    private AccelerometerDataEvent createPotholeEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        VerticalMotion verticalMotion = POTHOLE;
        int verticalMotionPercentage = findVerticalMotionPercentage(verticalMotion, accelerometerDataEventArrayList);
        return AccelerometerDataEvent.createVerticalMotionEvent(verticalMotion, verticalMotionPercentage);
    }

    /**
     *Cerca il valore della percentuale ottenuta dall'evento in base ad un movimento verticale particolare
     * @param verticalMotion    movemento verticale da cercare nella lista di eventi
     * @param accelerometerDataEventArrayList   lista di eventi
     * @return  il punteggio ottenuto da quel particolare movimento verticale
     */
    private int findVerticalMotionPercentage(VerticalMotion verticalMotion, ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        int percentage = 0;
        for(AccelerometerDataEvent event : accelerometerDataEventArrayList) {
            if(event.getType() != DIRECTION_EVENT && event.getVerticalMotion() == verticalMotion) {
                percentage = event.getVerticalMotionPercentage();
            }
        }
        return percentage;
    }

    /**
     * Cerca il valore della percentuale ottenuta dall'evento in base ad una direzione particolare
     * @param direction     direzione da cercare nella lista
     * @param accelerometerDataEventArrayList   lista di eventi
     * @return  il punteggio ottenuto da quella particolare direzione
     */
    private int findDirectionPercentage(Direction direction, ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        int percentage = 0;
        for(AccelerometerDataEvent event : accelerometerDataEventArrayList) {
            if(event.getType() != VERTICAL_MOTION_EVENT && event.getDirection() == direction) {
                percentage = event.getDirectionPercentage();
            }
        }
        return percentage;
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
     * Controlla se il tipo di evento che è appena arrivato dal sensore è una curva verso sinistra
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è una curva a sinistra
     */
    private boolean startOfLeftTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
                return false;
        }
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        float currentY = currentEvent.getY();
        float currentZ = currentEvent.getZ();
        return previousX == 0.0 && currentX < 0.0 && currentY < currentX && currentZ < currentX;
    }

    /**
     * Controlla se il tipo di evento che è appena arrivato dal sensore è una curva verso destra
     * @param coordinatesDataEventArrayList   lista degli eventi grezzi (senza elaborazione)
     * @return  true se è una curva a destra
     */
    private boolean startOfRightTurnEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }

        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousX = previousEvent.getX();
        float currentX = currentEvent.getX();
        float currentY = currentEvent.getY();
        float currentZ = currentEvent.getZ();
        return previousX == 0.0 && currentX > 0.0 && currentY < currentX && currentZ < currentX;
    }

    /**
     * Controlla il tipo di evento che è appena arrivato dal sensore
     * @param coordinatesDataEventArrayList   lista degli eventi
     * @return  true se l'ultimo è FORWARD
     */
    private boolean isAForwardEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        float currentZ = currentEvent.getZ();
        float previousZ = previousEvent.getZ();
        float currentX = currentEvent.getX();
        float currentY = currentEvent.getY();
        return previousZ == 0.0 && currentZ < 0.0 && currentX < currentZ && currentY < currentZ;
    }

    /**
     * Controlla il tipo di evento che è appena arrivato dal sensore
     * @param coordinatesDataEventArrayList   lista degli eventi
     * @return  true se l'ultimo è BACKARD
     */
    private boolean isABackwardEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
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
        return previousZ == 0.0 && currentZ > 0.0 && currentX < currentZ && currentY < currentZ;
    }

    private boolean startOfRoadBumpEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        float currentX = currentEvent.getX();
        float currentZ = currentEvent.getZ();
        return previousY == 0.0 && currentY > 0.0 && currentX < currentY && currentZ < currentY;
    }

    private boolean startOfPotholeEvent(ArrayList<CoordinatesDataEvent> coordinatesDataEventArrayList) {
        if(coordinatesDataEventArrayList.size() < 2) {
            return false;
        }
        CoordinatesDataEvent previousEvent = coordinatesDataEventArrayList.get(1);
        CoordinatesDataEvent currentEvent = coordinatesDataEventArrayList.get(0);
        float previousY = previousEvent.getY();
        float currentY = currentEvent.getY();
        float currentX = currentEvent.getX();
        float currentZ = currentEvent.getZ();
        return previousY == 0.0 && currentY < 0.0 && currentX < currentY && currentZ < currentY;
    }

    /**
     * Metodo per registrare il listener.
     * @param accelerometerDataEventListener    listener da registrare
     */
    void registerListener(AccelerometerDataEventListener accelerometerDataEventListener) {
        this.accelerometerDataEventListener = accelerometerDataEventListener;
    }

}
