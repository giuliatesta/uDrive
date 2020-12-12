package it.giuliatesta.udrive.accelerometer;

import java.io.Serializable;

/**
    Classe che rappresenta l'evento di modifica che accelerazione
 */
public class AccelerometerDataEvent implements Serializable {

    private Direction direction;
    private int directionPercentage;
    private VerticalMotion verticalMotion;
    private int verticalMotionPercentage;

    /**
     * Costruttore
     * @param direction
     * @param percentage
     * @param verticalMotion
     */
    public AccelerometerDataEvent(Direction direction, int percentage, VerticalMotion verticalMotion, int verticalMotionPercentage) {
        this.direction = direction;
        this.directionPercentage = percentage;
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
    }


    /**
     * Metodo get per la direzione indicata dall'accelerometro
     * @return direzione indicata dall'accelerometro
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Metodo get per il punteggio ottenuto
     * @return percentuale
     */
    public int getDirectionPercentage() {
        return directionPercentage;
    }

    public int getVerticalMotionPercentage() {
        return verticalMotionPercentage;
    }

    public VerticalMotion getVerticalMotion() {
        return verticalMotion;
    }

    @Override
    public String toString() {
        return "AccelerometerDataEvent{" +
                "direction=" + direction +
                ", directionPercentage=" + directionPercentage +
                "verticalMotion=" + verticalMotion +
                ", verticalMotionPercentage=" + verticalMotionPercentage +
                '}';
    }
}
