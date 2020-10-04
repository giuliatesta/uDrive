package it.giuliatesta.udrive;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class DataProcessor {

    int MaxValue;
    int MinValue;

    /*
        Calcola il modulo del vettore accelerazione
     */
    public double getAccelerationVector(double x, double y, double z) {
        return sqrt(pow(x,2) + pow(y,2) + pow(z,2));
    }

    /*
        Se il vettore supera il valore massimo oppure è minore del valore minino il punteggio è zero;
        mentre se rientra nel range viene calcolata una percentuale particolare
     */
    public int getPercentage(double vector) {
            int perc = 0;
            if(vector > MaxValue || vector < MinValue) {
                perc = 0;
            } else {
                perc = calculatePercentage(vector);
            }
            return perc;
    }
    /*
        Calcola una percentuale.
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

    public double getMediumValue(int max, int min) {
        return (max + min)/2;
    }

   /* public DataEvent calculateData(double x, double y, double z) {

    }*/

  //  DataEvent dataEvent = new DataEvent(Direction.LEFT, Acceleration.POSITIVE, 50);
}
