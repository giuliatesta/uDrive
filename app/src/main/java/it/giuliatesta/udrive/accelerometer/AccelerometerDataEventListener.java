package it.giuliatesta.udrive.accelerometer;
/*
    Interfaccia che definisce le operazioni che deve fare un Listener legato agli eventi di modifica dell'accelerometro
 */
public interface AccelerometerDataEventListener {
    void onDataChanged(AccelerometerDataEvent event);
}
