package it.giuliatesta.udrive.accelerometer;

import java.io.Serializable;

/**
    Classe che rappresenta l'evento di modifica che accelerazione
 */
public class AccelerometerDataEvent implements Serializable {

    private Direction direction;
    private Acceleration acceleration;
    private int percentage;

    /**
        Costruttore
     */
    public AccelerometerDataEvent(Direction direction, Acceleration acceleration, int percentage) {
        this.direction = direction;
        this.acceleration = acceleration;
        this.percentage = percentage;
    }

    /**
     * Metodo get per la direzione indicata dall'accelerometro
     * @return direzione indicata dall'accelerometro
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Metodo get per capire di che tipo è l'accelerazione
     * @return tipo di accelerazione
     */
    public Acceleration getAcceleration() {
        return acceleration;
    }

    /**
     * Metodo get per il punteggio ottenuto
     * @return percentuale
     */
    public int getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return "AccelerometerDataEvent{" +
                "direction=" + direction +
                ", acceleration=" + acceleration +
                ", percentage=" + percentage +
                '}';
    }
}
