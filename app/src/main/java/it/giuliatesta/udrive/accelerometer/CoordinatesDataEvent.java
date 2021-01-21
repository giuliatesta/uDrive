package it.giuliatesta.udrive.accelerometer;

/**
 * Classe intermedia per la rappresentazione degli eventi raccolti dal sensore
 */
public class CoordinatesDataEvent {

    private final float x;
    private final float y;
    private final float z;

    /**
     * Costruttore
     * @param x     coordinata x dell'accelerazione
     * @param y     coordinata y dell'accelerazione
     * @param z     coordinata z dell'accelerazione
     */
    public CoordinatesDataEvent(float x, float y, float z) {
        this.x = x;
        this.y = getAbsoluteYValue(y);
        this.z = getAbsoluteZValue(z);
    }

    private float getAbsoluteZValue(float z) {
        return (z - 0.812349F);
    }

    private float getAbsoluteYValue(float y) {
        return (y - 9.77631F);
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
