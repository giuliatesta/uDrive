package it.giuliatesta.udrive;

import it.giuliatesta.udrive.accelerometer.Acceleration;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;

import static it.giuliatesta.udrive.accelerometer.Acceleration.NEGATIVE;
import static it.giuliatesta.udrive.accelerometer.Acceleration.POSITIVE;
import static it.giuliatesta.udrive.accelerometer.Acceleration.ZERO;
import static it.giuliatesta.udrive.accelerometer.Direction.LEFT;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
/*
    Classe che si occupa di elaborare i dati del sensore e generare i dati usati dalla UI
    x = indica destra sinistra
    y = indica sopra sotto
    z = indica avanti indietro
 */
public class DataProcessor {

    private int MaxValue = 10;
    private int MinValue = -10;

    /*
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
    /*
        Calcola la direzione
     */
    private Direction getDirection(double x, double z) {

        double vect = abs(sqrt((pow(2, x) + pow(2, z))));
        double alpha = 0.0;
        return LEFT;
       /*if (x == 0) {
            alpha = getAngleUsingOneCoordinate(vect, z);
        } else if(z == 0) {
            alpha = getAngleUsingOneCoordinate(vect, x);
        }


        if (alpha >= pi4 && alpha <= pi34) {
            return FORWARD;
        } else if (alpha >= pi54 && alpha <= pi74) {
            return BACKWARD;
        } else if ((alpha >= 0 && alpha < pi4) || (alpha > pi74 && alpha <= 2*PI)) {
            return RIGHT;
        } else if (alpha > pi34 && alpha < pi54) {
            return LEFT;
        } else {
            return DEFAULT;
        }*/
    }

    private double getAngleUsingOneCoordinate(double vect, double coordinate) {
        return Math.asin(coordinate / vect);            //restituisce l'angolo di inclinazione del vettore in 2D in radianti
    }


    /*
        Se il vettore supera il valore massimo oppure è minore del valore minino il punteggio è zero;
        mentre se rientra nel range viene calcolata una percentuale particolare
     */
    private int getPercentage(double vector) {
            if(vector > MaxValue || vector < MinValue) {
                // Se si trova fuori dall'intervallo
                return 0;
            } else {
                // Se si trova dentro, chiama il metodo per calcolare la percentuale
                return calculatePercentage(vector);
            }
    }

    /*
        Calcola una percentuale
     */
    private int calculatePercentage(double vector) {
        // Calcola il valore medio
        double mediumValue = getMediumValue(MaxValue, MinValue);
        // Calcola il valore dell'intervallo dei valori
        double range = MaxValue - MinValue;
        // rapporto tra la variazione dal valor medio e l'intervallo di valori
        double ratio = abs(vector - mediumValue)/range;
        // La percentuale è il rapporto per 100
        return (int) (ratio*100);
    }

    /*
        Calcola il valore medio dell'intervallo come differenza del
        valore massimo e del minimo divisa per due
     */
    private double getMediumValue(int max, int min) {
        return (max + min)/2;
    }

    /*
        Calcola il tipo di accelerazione in base al vettore risultante
     */
    private Acceleration getAcceleration(double vector) {
        if(vector > 0) {
            // Se il vettore risultante è positivo c'è una accelerazione positiva
            return POSITIVE;
        } else if (vector < 0) {
            // Se è negativo c'è una accelerazione negativa
            return NEGATIVE;
        } else {
            // Altrimenti non sta accelerando
            return ZERO;
        }
    }

    /*
        Produce il dataEvent chiamando tutti i metodi che permettono di creare tutte le informazioni
     */
    public AccelerometerDataEvent calculateData(double x, double y, double z) {
        // Calcola il vettore accelerazione
        double vector = getAccelerationVector(x, y, z);
        // Calcola la direzione
        Direction direction = getDirection(x, z);
        // Calcola il tipo di accelerazione
        Acceleration acceleration = getAcceleration(vector);
        // Calcola la percentuale
        int percentage = getPercentage(vector);
        // Genera l'avento
        return new AccelerometerDataEvent(direction, acceleration, percentage);
    }

}
