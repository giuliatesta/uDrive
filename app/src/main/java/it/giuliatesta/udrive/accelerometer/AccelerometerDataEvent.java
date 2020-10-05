package it.giuliatesta.udrive.accelerometer;

/*
    Classe che rappresenta l'evento di modifica che accelerazione
 */
public class AccelerometerDataEvent {

    public Direction direction;
    public Acceleration acceleration;
    public int percentage;

    /*
        Costruttore
     */
    public AccelerometerDataEvent(Direction direction, Acceleration acceleration, int percentage) {
        this.direction = direction;
        this.acceleration = acceleration;
        this.percentage = percentage;
    }

    public Direction getDirection() {
        return direction;
    }

    public Acceleration getAcceleration() {
        return acceleration;
    }

    public int getPercentage() {
        return percentage;
    }
}
