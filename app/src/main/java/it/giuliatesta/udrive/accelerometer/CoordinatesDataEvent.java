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
        this.y = y;
        this.z = z;
    }

    /**
     * Filtro passa alto applicato ai valori che arrivano dall'accelerometro
     * @param input     coordinata x
     * @param output     coordinata y
     * @return      valori filtrati
     */
    public static float[] lowPassFiltering(float[] input, float[] output) {
        float alpha = 0.5F;
        if(output == null) {
            return input;
        }

        for(int i = 0; i < input.length ; i++) {
            output[i] = output[i] + alpha * (input[i]-output[i]);
        }
        return output;
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
