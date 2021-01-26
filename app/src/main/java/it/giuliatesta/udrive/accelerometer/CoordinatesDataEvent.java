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

    /**
     * Resistuisce il valore della coordinata z senza il valore iniziale di disturbo
     * @param z     coordinata con il disturbo
     * @return      coordinata senza il disturbo
     */
    private float getAbsoluteZValue(float z) {
        return (z - 0.812349F);
    }

    /**
     * Restituisce il valore della coordinata y senza il valore iniziale che considera l'accelerazione di gravità
     * @param y     coordinata con accelerazione di gravità
     * @return      coordinata senza accelerazione di gravità
     */
    private float getAbsoluteYValue(float y) {
        return (y - 9.77631F);
    }

    /**
     * Metodo get per la coordinata x
     * @return  coordinata x
     */
    public float getX() {
        return x;
    }

    /**
     * Metodo get per la coordinata y
     * @return  coordinata y
     */
    public float getY() {
        return y;
    }

    /**
     * Metodo get per la coordinata z
     * @return  coordinata z
     */
    public float getZ() {
        return z;
    }
}
