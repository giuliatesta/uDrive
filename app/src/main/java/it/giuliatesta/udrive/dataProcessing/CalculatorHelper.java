package it.giuliatesta.udrive.dataProcessing;

import it.giuliatesta.udrive.accelerometer.Direction;
import it.giuliatesta.udrive.accelerometer.VerticalMotion;

import static it.giuliatesta.udrive.dataProcessing.Constants.MaxValue;
import static it.giuliatesta.udrive.dataProcessing.Constants.MinValue;
import static it.giuliatesta.udrive.dataProcessing.Constants.fourtyFiveDegree;
import static it.giuliatesta.udrive.dataProcessing.Constants.oneHundredThirtyFive;
import static it.giuliatesta.udrive.dataProcessing.Constants.threeHundredFifteen;
import static it.giuliatesta.udrive.dataProcessing.Constants.threeHundredSixty;
import static it.giuliatesta.udrive.dataProcessing.Constants.twoHundredTwentyFive;
import static it.giuliatesta.udrive.dataProcessing.Constants.zeroDegree;
import static it.giuliatesta.udrive.accelerometer.Direction.BACKWARD;
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
 * Classe di supporto per il calcolo della direzione, del movimento verticale e dei loro rispettivi punteggi
 */
class CalculatorHelper {


    /**
     Calcola il modulo del vettore accelerazione
     */
    static double getAccelerationVector(double x, double y, double z) {

        // Calcola la radice quadrata della somma dei quadrati delle coordinate
        double absVector = sqrt(pow(x,2) + pow(y, 2) + pow(z,2));
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
    private static double getPositionOfAlpha(double x, double z) {
        double alpha = Math.toDegrees(atan2(z, x));     //Angolo in gradi
        return ((int) (alpha+360))%360;
    }

    /**
     Calcola la direzione
     @param x  coordinata x dell'accelerazione
     @param z  coordinata z dell'accelerazione
     */
    static Direction getDirection(double x, double z) {
        Direction direction = Direction.NONE;
        double alpha = getPositionOfAlpha(x, z);

        if (alpha >= fourtyFiveDegree && alpha <= oneHundredThirtyFive) {
            // Se l'angolo è compreso tra 45 e 135
            direction = FORWARD;
        } else if (alpha >= twoHundredTwentyFive && alpha <= threeHundredFifteen) {
            // Se l'angolo è compreso tra 225 e 315
            direction = BACKWARD;
        } else if ((alpha > threeHundredFifteen && alpha <= threeHundredSixty) || (alpha >= zeroDegree && alpha < fourtyFiveDegree)) {
            // Se l'angolo è compreso tra -45 e 45
            direction = RIGHT;
        } else if (alpha > oneHundredThirtyFive && alpha < twoHundredTwentyFive) {
            direction = LEFT;
        }
        return direction;
    }

    /**
     Calcola una percentuale. Fa la proporzione x : 100 = vector : MaxValue
     @param vector vettore accelerazione
     @return percentuale
     */
    private static int calculatePercentage(double vector) {
        return (int) ((vector*100)/MaxValue);
    }

    /**
     Se il vettore supera il valore massimo oppure è minore del valore minino il punteggio è zero;
     mentre se rientra nel range viene calcolata una percentuale particolare
     @param vector vettore accelerazione
     @return percentuale ottenuta
     */
    static int getPercentage(double vector) {
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
     * Calcola il movimento verticale
     * @param y coomponente y del vettore accelerazione
     * @return corrispondente movimento verticale
     */
    static VerticalMotion getVerticalMotion(double y) {
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
    static int getVerticalMotionPercentage(VerticalMotion verticalMotion, double y) {
        if (verticalMotion != NONE) {
            return CalculatorHelper.getPercentage(y);
        } else {
            return 0;
        }
    }
}
