package it.giuliatesta.udrive.dataProcessing;

/**
 * Singleton utilizzato per contenere i valori delle soglie necessarie per identificare i movimenti
 */
public enum Configuration {

    INSTANCE;

    private double minValueX = 1.0;
    private double minValueY = 1.0;
    private double minValueZ = 1.0;
    private double maxValue = 15.0;

    private double maxMinValueSeekBar = 2.0;
    private double maxMaxValueSeekBar = 20.0;

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

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Calcola la proporzione per trovare il nuovo valore della soglia minima in funzione del progress del seekBar
     * @param progress      numero intero tra 0 e 100
     * @return  nuova soglia minima
     */
    public double calculateProportionForMinValue(int progress) {
        // minValue : 100 = x : progress
        return ((progress * maxMinValueSeekBar)/100.0);
    }

    /**
     * Calcola la proporzione per trovare il nuovo valore della soglia massima in funzione del progress del seekBar
     * @param progress      numero intero tra 0 e 100
     * @return  nuova soglia massima
     */
    public double calculateProportionForMaxValue(int progress) {
        // maxValue : 100 = x : progress
        return ((progress * maxMaxValueSeekBar)/100.0);
    }

    /**
     * Calcola la soglia minima media tra quelle delle coordinare x, y, z
     * @return  media delle soglie minime
     */
    public double calculateMediumMinValue() {
        double sum = minValueX + minValueY + minValueZ;
        return (sum/3.0);
    }
}
