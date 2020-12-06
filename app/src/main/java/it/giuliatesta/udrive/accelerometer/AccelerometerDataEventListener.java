package it.giuliatesta.udrive.accelerometer;
/**
    Interfaccia che definisce le operazioni che deve fare un Listener legato agli eventi di modifica dell'accelerometro
 */
public interface AccelerometerDataEventListener {

    /**
     * Viene chiamato quando viene ricevuto un evento di tipo AccelerometerDataEvent
     * @param event evento legato all'accelerometro
     */
    void onDataChanged(AccelerometerDataEvent event);
}
