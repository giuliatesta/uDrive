package it.giuliatesta.udrive;

import it.giuliatesta.udrive.accelerometer.Acceleration;
import it.giuliatesta.udrive.accelerometer.DataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class DataProcessor {

    int MaxValueRange;
    int MinValueRange;

    // Calcola il modulo del vettore accelerazione
    public double getAccelerationVector(double x, double y, double z) {
        return sqrt(pow(x,2) + pow(y,2) + pow(z,2));
    }

    public int getPercentage(double vector) {
            int perc = 0;
            if(vector > MaxValueRange || vector < MinValueRange) {
                perc = 0;
            } else {
                //calculatePercentage();
            }
            return perc;
    }

    public double getMediumValue(int max, int min) {
        return (max + min)/2;
    }

   /* public DataEvent calculateData(double x, double y, double z) {

    }*/

  //  DataEvent dataEvent = new DataEvent(Direction.LEFT, Acceleration.POSITIVE, 50);
}
