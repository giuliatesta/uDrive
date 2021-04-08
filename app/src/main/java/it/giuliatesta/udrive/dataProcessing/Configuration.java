package it.giuliatesta.udrive.dataProcessing;

import static it.giuliatesta.udrive.dataProcessing.Constants.MinValue;

/**
 * Singleton
 */
public enum Configuration {

    INSTANCE;

    private double minValueX = 1.0;
    private double minValueY = 1.0;
    private double minValueZ = 1.0;

    public double getMinValueX() {
        return minValueX;
    }

    public void setMinValueX(double minValueX) {
        this.minValueX = minValueX;
    }

    public double getMinValueY() {
        return minValueY;
    }

    public void setMinValueY(double minValueY) {
        this.minValueY = minValueY;
    }

    public double getMinValueZ() {
        return minValueZ;
    }

    public void setMinValueZ(double minValueZ) {
        this.minValueZ = minValueZ;
    }

    public double calculateProportion(int progress) {
        // minValue : 100 = x : progress
        return ((progress * MinValue)/100.0);
    }
}
