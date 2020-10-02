package it.giuliatesta.udrive.accelerometer;

public class DataEvent {

    public Direction direction;
    public Acceleration acceleration;
    public int perc;

    public DataEvent(Direction direction, Acceleration acceleration, int perc) {
        this.direction = direction;
        this.acceleration = acceleration;
        this.perc = perc;
    }

    public Direction getDirection() {
        return direction;
    }

    public Acceleration getAcceleration() {
        return acceleration;
    }

    public int getPerc() {
        return perc;
    }
}
