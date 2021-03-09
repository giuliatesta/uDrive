package it.giuliatesta.udrive.accelerometer;

import static it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent.DeviceOrientation.HORIZONTAL;
import static it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent.DeviceOrientation.VERTICAL;

/**
 * Classe intermedia per la rappresentazione degli eventi raccolti dal sensore
 */
public class CoordinatesDataEvent {

    private final float x;
    private final float y;
    private final float z;
    public enum DeviceOrientation {
        VERTICAL, HORIZONTAL;
    };

    private static DeviceOrientation deviceOrientation = VERTICAL;
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
        float alpha = 0.25F;
        if(output == null) {
            return input;
        }

        for(int i = 0; i < input.length ; i++) {
            output[i] = output[i] + alpha * (input[i]-output[i]);
        }
        return output;
    }

    public static float[] setOrientation(float[] input, DeviceOrientation deviceOrientation) {
        if(deviceOrientation == HORIZONTAL) {
            // Se il dispositivo è utilizzato orizzontalmente, la coordinata z e la coordinata y sono da scambiare
            float temp = input[1];
            input[1] = input[2];
            input[2] = temp;
        }
        return input;
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


    public void setDeviceOrientation(DeviceOrientation deviceOrientation) {
        this.deviceOrientation = deviceOrientation;
    }

    public static DeviceOrientation getDeviceOrientation() {
        return deviceOrientation;
    }
}
