package it.giuliatesta.udrive;

import android.hardware.SensorEvent;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
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
import static it.giuliatesta.udrive.accelerometer.Direction.BACKWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.DEFAULT;
import static it.giuliatesta.udrive.accelerometer.Direction.FORWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.LEFT;
import static it.giuliatesta.udrive.accelerometer.Direction.RIGHT;
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
public class DataProcessor {

    private AccelerometerDataEventListener accelerometerDataEventListener;
    public enum AnalyzeResult { PROCESSED, NEED_OTHER_EVENTS }
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
        double ratio = z/x;
        double alpha = Math.toDegrees(atan2(z, x));     //Angolo in gradi
        return ((int) (alpha+360))%360;
    }

    /**
        Calcola la direzione
     @param x  coordinata x dell'accelerazione
     @param z  coordinata z dell'accelerazione
     */
    private Direction getDirection(double x, double z) {
        Direction direction = DEFAULT;
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
        Calcola il valore medio dell'intervallo come differenza del
        valore massimo e del minimo divisa per due
        @param min valore minimo
        @param max valore massimo
        @return valore medio
     */
    private double getMediumValue(double max, double min) {
        return (max + min)/2;
    }

    /**
     * Calcola il movimento verticale
     * @param y coomponente y del vettore accelerazione
     * @return corrispondente movimento verticale
     */
    private VerticalMotion getVerticalMotion(double y) {
        VerticalMotion verticalMotion = NONE;
        double yWithoutGravity = y - 9.78;
        if (yWithoutGravity > MinValue) {
            verticalMotion = ROADBUMP;
        } else if (yWithoutGravity < -MinValue) {
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
    public AccelerometerDataEvent calculateData(double x, double y, double z) {
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

    public AnalyzeResult analyze(ArrayList<SensorEvent> sensorEventArrayList) {
        ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList = generateAccelerometerEvents(sensorEventArrayList);
        if (singleEvent(accelerometerDataEventArrayList)) {
            // crea evento
            // lancia evento
        } else if (leftEvent(accelerometerDataEventArrayList)) {
            AccelerometerDataEvent leftTurnEvent = createLeftEvent(accelerometerDataEventArrayList);
            accelerometerDataEventListener.onDataChanged(leftTurnEvent);
        }
        return NEED_OTHER_EVENTS;
    }

    private AccelerometerDataEvent createLeftEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        Direction direction = LEFT;
        int directionPercentage = calculateMediumValue(accelerometerDataEventArrayList);
        return AccelerometerDataEvent.createDirectionEvent(direction, directionPercentage);
    }

    private int calculateMediumValue(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        double sum = 0.0;
        for (AccelerometerDataEvent event : accelerometerDataEventArrayList) {
            sum += event.getDirectionPercentage();
        }
        return (int) (sum / 4);
    }

    private ArrayList<AccelerometerDataEvent> generateAccelerometerEvents(ArrayList<SensorEvent> sensorEventArrayList) {
        ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList = new ArrayList<>();
        for(SensorEvent event : sensorEventArrayList) {
            AccelerometerDataEvent accelerometerDataEvent = calculateData(event.values[0], event.values[1], event.values[2]);
            accelerometerDataEventArrayList.add(0, accelerometerDataEvent);
        }
        return accelerometerDataEventArrayList;
    }

    private boolean leftEvent(ArrayList<AccelerometerDataEvent> accelerometerDataEventArrayList) {
        AccelerometerDataEvent event0 = accelerometerDataEventArrayList.get(0);
        AccelerometerDataEvent event1 = accelerometerDataEventArrayList.get(1);
        AccelerometerDataEvent event2 = accelerometerDataEventArrayList.get(2);
        AccelerometerDataEvent event3 = accelerometerDataEventArrayList.get(3);
        if(event0.getDirection() == LEFT && event1.getVectorValue() < MinValue &&
                event2.getDirection() == RIGHT && event3.getVectorValue() < MinValue) {
            return true;
        }
        return false;
    }


    private boolean singleEvent(ArrayList<AccelerometerDataEvent> sensorEventArrayList) {
        return false;
    }

}
