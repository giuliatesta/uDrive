package it.giuliatesta.udrive.accelerometer;

/**
 * Enumerazione per definire il tipo di evento dell'accelerometro
 */
public enum EventType {
    /**
     * BOTH: se l'evento mostra movimenti sia orizzontali (direzioni di movimento) che movimenti verticali
     * DIRECTION_EVENT: se l'evento mostra solo una direzione
     * VERTICAL_MOTION_EVENT: se l'evento mostra solo un movimento verticale
     */
    BOTH, DIRECTION_EVENT, VERTICAL_MOTION_EVENT

}
