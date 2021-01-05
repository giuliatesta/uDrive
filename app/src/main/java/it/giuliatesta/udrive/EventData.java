package it.giuliatesta.udrive;

public class EventData {

    private final float x;
    private final float y;
    private final float z;

    public EventData(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
