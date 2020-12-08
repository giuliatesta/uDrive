package it.giuliatesta.udrive;

import it.giuliatesta.udrive.accelerometer.Acceleration;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;

import static it.giuliatesta.udrive.Constants.fourtyFiveDegree;
import static it.giuliatesta.udrive.Constants.ninetyDegree;
import static it.giuliatesta.udrive.Constants.oneHundredEightyDegree;
import static it.giuliatesta.udrive.Constants.oneHundredThirtyFive;
import static it.giuliatesta.udrive.Constants.threeHundredFifteen;
import static it.giuliatesta.udrive.Constants.threeHundredSixty;
import static it.giuliatesta.udrive.Constants.twoHundredSeventyDegree;
import static it.giuliatesta.udrive.Constants.twoHundrenTwentyFive;
import static it.giuliatesta.udrive.Constants.zeroDegree;
import static it.giuliatesta.udrive.accelerometer.Acceleration.NEGATIVE;
import static it.giuliatesta.udrive.accelerometer.Acceleration.POSITIVE;
import static it.giuliatesta.udrive.accelerometer.Acceleration.ZERO;
import static it.giuliatesta.udrive.accelerometer.Direction.BACKWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.DEFAULT;
import static it.giuliatesta.udrive.accelerometer.Direction.FORWARD;
import static it.giuliatesta.udrive.accelerometer.Direction.LEFT;
import static it.giuliatesta.udrive.accelerometer.Direction.RIGHT;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
/**
    Classe che si occupa di elaborare i dati del sensore e generare i dati usati dalla UI
    x = indica destra sinistra
    y = indica sopra sotto
    z = indica avanti indietro
 */
public class DataProcessor {

    private int MaxValue = 10;
    private int MinValue = -10;

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
     TO DO: RISOLVERE I PROBLEMI LEGATI A QUESTO ALGORITMO
     Calcola la posizione dell'angolo nei 4 quadranti
     */
    private double getPositionOfAlpha(double x, double z) {
        double ratio = z/x;
        double alpha = Math.toDegrees(atan(ratio));     //Angolo in gradi

        // Visto che restituisce un angolo tra 0 e 90 gradi bisogna capire in quale quadrante si trova
        // Consideriamo prima i casi particolari
        if (x == 0) {
            if (z > 0) {
                // novanta gradi
                return ninetyDegree;
            } else if (z == 0) {
                // zero
                return zeroDegree;
            } else {
                // duecentosettanta gradi
                return twoHundredSeventyDegree;
            }
        }

        if (x < 0 && z < 0) {
            // Terzo quadrante
            alpha += oneHundredEightyDegree;
        } else if(x < 0) {
            // Secondo quadrante
            alpha += ninetyDegree;
        } else if (z < 0) {
            // Quarto quadrante
            alpha += twoHundredSeventyDegree;
        }
        return alpha;
    }

    /**
        Calcola la direzione
     @param x  coordinata x dell'accelerazione
     @param z  coordinata z dell'accelerazione
     */
    private Direction getDirection(double x, double z) {
        double alpha = getPositionOfAlpha(x, z);
        if (alpha >= fourtyFiveDegree && alpha <= oneHundredThirtyFive) {
            // Se l'angolo è compreso tra 45 e 135
            return FORWARD;
        } else if (alpha >= twoHundrenTwentyFive && alpha <= threeHundredFifteen) {
            // Se l'angolo è compreso tra 225 e 315
            return BACKWARD;
        } else if ((alpha > threeHundredFifteen && alpha <= threeHundredSixty) || (alpha >= zeroDegree && alpha < fourtyFiveDegree)) {
            // Se l'angolo è compreso tra -45 e 45
            return RIGHT;
        } else if (alpha > oneHundredThirtyFive && alpha < twoHundrenTwentyFive) {
            return LEFT;
        } else {
            return DEFAULT;
        }
    }
    /**
        Se il vettore supera il valore massimo oppure è minore del valore minino il punteggio è zero;
        mentre se rientra nel range viene calcolata una percentuale particolare
        @param vector vettore accelerazione
        @return percentuale ottenuta
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

    /**
        Calcola una percentuale
        @param vector vettore accelerazione
        @return percentuale
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
        Calcola il tipo di accelerazione in base al vettore risultante
        @param vector vettore accelerazione
        @return tipo di accelerazione
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
        // Calcola il tipo di accelerazione
        Acceleration acceleration = getAcceleration(vector);
        // Calcola la percentuale
        int percentage = getPercentage(vector);
        // Genera l'avento
        return new AccelerometerDataEvent(direction, acceleration, percentage);
    }


}
