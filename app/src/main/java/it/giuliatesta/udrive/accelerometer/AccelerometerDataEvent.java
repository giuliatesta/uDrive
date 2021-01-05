package it.giuliatesta.udrive.accelerometer;

import java.io.Serializable;

import static it.giuliatesta.udrive.accelerometer.Direction.DEFAULT;
import static it.giuliatesta.udrive.accelerometer.EventType.BOTH;
import static it.giuliatesta.udrive.accelerometer.EventType.DIRECTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.EventType.VERTICAL_MOTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.NONE;

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
     * Costruttore vuoto
     */
    public AccelerometerDataEvent() {
    }

    /**
     * Costruttore di un evento completo
     * @param direction tipo di direzione
     * @param directionPercentage   punteggio associato alla direzione
     * @param verticalMotion    tipo di movimento verticale
     * @param verticalMotionPercentage  punteggio associato al movimento
     * @param vectorValue modulo del vettore accelerazione
     */
    public AccelerometerDataEvent(Direction direction, int directionPercentage, VerticalMotion verticalMotion, int verticalMotionPercentage, double vectorValue) {
        this.direction = direction;
        this.directionPercentage = directionPercentage;
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        this.vectorValue = vectorValue;
        type = BOTH;
    }

    /**
     * Costruttore di un evento completo, senza il valore del vettore accelerazione
     * @param direction tipo di direzione
     * @param directionPercentage   punteggio associato alla direzione
     * @param verticalMotion    tipo di movimento verticale
     * @param verticalMotionPercentage  punteggio associato al movimento
     */
    public AccelerometerDataEvent(Direction direction, int directionPercentage, VerticalMotion verticalMotion, int verticalMotionPercentage) {
        this.direction = direction;
        this.directionPercentage = directionPercentage;
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        type = BOTH;
    }

    /**
     * Metodo per la creazione di un evento di tipo DIRECTION_EVENT
     * @param direction     tipo di direzione
     * @param directionPercentage   punteggio associato alla direzione
     * @return
     */
    public static AccelerometerDataEvent createDirectionEvent(Direction direction, int directionPercentage) {
        return new AccelerometerDataEvent(direction, directionPercentage);
    }

    /**
     * Metodo per la creazione di un evento di tipo VERTICAL_MOTION_EVENT
     * @param verticalMotion    tipo di movimento verticale
     * @param verticalMotionPercentage punteggio associato al movimento
     * @return
     */
    public static AccelerometerDataEvent createVerticalMotionEvent(VerticalMotion verticalMotion, int verticalMotionPercentage) {
        return new AccelerometerDataEvent(verticalMotion, verticalMotionPercentage);
    }


    /**
     * Costrutore per un evento di tipo DIRECTION_EVENT
     * @param direction tipo di direzione
     * @param directionPercentage punteggio associato alla direzione
     */
    private AccelerometerDataEvent(Direction direction, int directionPercentage) {
        this.direction = direction;
        this.directionPercentage = directionPercentage;
        this.verticalMotion = NONE;
        this.verticalMotionPercentage = 0;
        type = DIRECTION_EVENT;

    }

    /**
     * Costruttore per un evento di tipo VERTICAL_MOTION_EVENT
     * @param verticalMotion    tipo di movimento
     * @param verticalMotionPercentage  punteggio associato al movimento
     */
    private AccelerometerDataEvent(VerticalMotion verticalMotion, int verticalMotionPercentage) {
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        this.direction = DEFAULT;
        this.directionPercentage = 0;
        type = VERTICAL_MOTION_EVENT;
    }

    /**
     * Metodo get per la direzione indicata dall'accelerometro
     * Non dovrebbe mai accadere che viene richiesta la direction quando l'evento è ti tipo VERTICAL_MOTION_EVENT
     * @return direzione indicata dall'accelerometro
     */

    public Direction getDirection() {
        if (type == VERTICAL_MOTION_EVENT) {
            throw new IllegalArgumentException("IMPOSSIBLE. Its type is VERTICAL_MOTION_EVENT!");
        } else {
            return direction;
        }
    }

    /**
     * Metodo get per il punteggio ottenuto della direzione
     * Non dovrebbe mai accadere che viene richiesta la directionPercentage quando l'evento è ti tipo VERTICAL_MOTION_EVENT
     * @return percentuale
     */

    public int getDirectionPercentage() {
        if (type == VERTICAL_MOTION_EVENT) {
            throw new IllegalArgumentException("IMPOSSIBLE. Its type is VERTICAL_MOTION_EVENT!");
        } else {
            return directionPercentage;
        }
    }

    /**
     * Metodo get per il punteggio ottenuto del movimento verticale
     * Non dovrebbe mai accadere che viene richiesta la verticalMotionPercentage quando l'evento è ti tipo DIRECTION_EVENT
     * @return percentuale
     */
    public int getVerticalMotionPercentage() {
        if (type == DIRECTION_EVENT) {
            throw new IllegalArgumentException("IMPOSSIBLE. Its type is DIRECTION_EVENT!");
        } else {
            return verticalMotionPercentage;
        }
    }

    /**
     * Metodo get per il tipo di movimento verticale
     * Non dovrebbe mai accadere che viene richiesta la verticalMotion quando l'evento è ti tipo DIRECTION_EVENT
     * @return movimento verticale
     */
    public VerticalMotion getVerticalMotion() {
        if (type == DIRECTION_EVENT) {
            throw new IllegalArgumentException("IMPOSSIBLE. Its type is DIRECTION_EVENT!");
        } else {
            return verticalMotion;
        }
    }

    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * Metodo get per il tipo di evento
     * @return tipo di evento
     */
    public EventType getType() {
        return type;
    }

    /**
     * Metodo get per il valore del modulo dell'accelerazione
     * @return modulo dell'accelerazione
     */
    public double getVectorValue() {
        return vectorValue;
    }
    @Override
    public String toString() {
        return "AccelerometerDataEvent{" +
                "direction = " + direction +
                ", directionPercentage = " + directionPercentage +
                ", verticalMotion = " + verticalMotion +
                ", verticalMotionPercentage = " + verticalMotionPercentage +
                ", type = " + type +
                '}';
    }
}
