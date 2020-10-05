package it.giuliatesta.udrive;

import it.giuliatesta.udrive.accelerometer.Acceleration;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;

import static it.giuliatesta.udrive.accelerometer.Acceleration.NEGATIVE;
import static it.giuliatesta.udrive.accelerometer.Acceleration.POSITIVE;
import static it.giuliatesta.udrive.accelerometer.Acceleration.ZERO;
import static it.giuliatesta.udrive.accelerometer.Direction.LEFT;
import static it.giuliatesta.udrive.accelerometer.Direction.RIGHT;
import static it.giuliatesta.udrive.accelerometer.Direction.STRAIGHT;
import static java.lang.Math.*;
import static java.lang.Math.pow;
/*
    Classe che si occupa di elaborare i dati del sensore e generare i dati usati dalla UI
 */
public class DataProcessor {

    int MaxValue;
    int MinValue;

    /*
        Calcola il modulo del vettore accelerazione
     */
    public double getAccelerationVector(double x, double y, double z) {
        // Calcola la radice quadrata della somma dei quadrati delle coordinate
        double absVector = sqrt(pow(x,2) + pow(y,2) + pow(z,2));
        if(y > 0) {
            // Se la componente y è positiva significa che sta accelerando nel senso di marcia --> accelera
            return absVector;
        } else {
            // Altrimenti sta decelerando
            return (-absVector);
        }
    }
    /*
        Calcola la direzione
     */
    public Direction getDirection(double x, double y) {
        if (x > 0) {
            // Se si trova nel primo o nel quarto quadrante sta girando a destra
            return RIGHT;
        } else if (x < 0) {
            // Se si trova nel secondo o terzo quadrante sta girando a sinistra
            return LEFT;
        } else {
            // Altrimenti sta andando dritto
            return STRAIGHT;
        }
    }
    /*
        Se il vettore supera il valore massimo oppure è minore del valore minino il punteggio è zero;
        mentre se rientra nel range viene calcolata una percentuale particolare
     */
    public int getPercentage(double vector) {
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
        double ratio = (abs(vector - mediumValue))/range;
        // La percentuale è il rapporto per 100
        return (int) (ratio*100);
    }

    /*
        Calcola il valore medio dell'intervallo come differenza del
        valore massimo e del minimo divisa per due
     */
    public double getMediumValue(int max, int min) {
        return (max - min)/2;
    }

    /*
        Produce il dataEvent chiamando tutti i metodi che permettono di creare tutte le informazioni
     */
    public AccelerometerDataEvent calculateData(double x, double y, double z) {
        // Calcola il vettore accelerazione
        double vector = getAccelerationVector(x, y, z);
        // Calcola la direzione
        Direction direction = getDirection(x, y);
        // Calcola il tipo di accelerazione
        Acceleration acceleration = getAcceleration(vector);
        // Calcola la percentuale
        int percentage = calculatePercentage(vector);
        // Genera l'avento
        AccelerometerDataEvent accelerometerDataEvent = new AccelerometerDataEvent(direction, acceleration, percentage);
        return accelerometerDataEvent;
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
}
