package it.giuliatesta.udrive.accelerometer;

import java.io.Serializable;

import static it.giuliatesta.udrive.accelerometer.EventType.BOTH;
import static it.giuliatesta.udrive.accelerometer.EventType.DIRECTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.EventType.VERTICAL_MOTION_EVENT;

/**
    Classe che rappresenta l'evento di modifica che accelerazione
 */
public class AccelerometerDataEvent implements Serializable {

    private Direction direction;
    private int directionPercentage;
    private VerticalMotion verticalMotion;
    private int verticalMotionPercentage;
    private double vectorValue;
    private EventType type;

    /**
     * Costruttore
     * @param direction
     * @param percentage
     * @param verticalMotion
     */
    public AccelerometerDataEvent(Direction direction, int percentage, VerticalMotion verticalMotion, int verticalMotionPercentage, double vectorValue) {
        this.direction = direction;
        this.directionPercentage = percentage;
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        this.vectorValue = vectorValue;
        type = BOTH;
    }

    public AccelerometerDataEvent(Direction direction, int percentage, VerticalMotion verticalMotion, int verticalMotionPercentage) {
        this.direction = direction;
        this.directionPercentage = percentage;
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        type = BOTH;
    }

    public static AccelerometerDataEvent createDirectionEvent(Direction direction, int directionPercentage) {
        return new AccelerometerDataEvent(direction, directionPercentage);
    }

    public static AccelerometerDataEvent createVerticalMotionEvent(VerticalMotion verticalMotion, int verticalMotionPercentage) {
        return new AccelerometerDataEvent(verticalMotion, verticalMotionPercentage);
    }

    private AccelerometerDataEvent(Direction direction, int directionPercentage) {
        this.direction = direction;
        this.directionPercentage = directionPercentage;
        type = DIRECTION_EVENT;
    }

    private AccelerometerDataEvent(VerticalMotion verticalMotion, int verticalMotionPercentage) {
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        type = VERTICAL_MOTION_EVENT;
    }

    /**
     * Metodo get per la direzione indicata dall'accelerometro
     * @return direzione indicata dall'accelerometro
     */
    /*
    public Direction getDirection() {
        if(type == DIRECTION_EVENT) {
            return direction;
        } else {
            throw new IllegalArgumentException("IMPOSSIBLE. This is a VerticalMotionEvent!");
        }
    }
*/

    public Direction getDirection() {
        return direction;
    }

    public int getDirectionPercentage() {
        return directionPercentage;
    }

    public VerticalMotion getVerticalMotion() {
        return verticalMotion;
    }

    public int getVerticalMotionPercentage() {
        return verticalMotionPercentage;
    }

    /**
     * Metodo get per il punteggio ottenuto
     * @return percentuale
     */
    /*
    public int getDirectionPercentage() {
        if(type == DIRECTION_EVENT) {
            return directionPercentage;
        } else {
            throw new IllegalArgumentException("IMPOSSIBLE. This is a VerticalMotionEvent!");
        }
    }

    public int getVerticalMotionPercentage() {
        if(type == VERTICAL_MOTION_EVENT) {
            return verticalMotionPercentage;
        } else {
            throw new IllegalArgumentException("IMPOSSIBLE. This is a DirectionEvent!");
        }
    }

    public VerticalMotion getVerticalMotion() {
        if(type == VERTICAL_MOTION_EVENT) {
            return verticalMotion;
        } else {
            throw new IllegalArgumentException("IMPOSSIBLE. This is a DirectionEvent!");
        }
    }*/


    public EventType getType() {
        return type;
    }

    public double getVectorValue() {
        return vectorValue;
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
