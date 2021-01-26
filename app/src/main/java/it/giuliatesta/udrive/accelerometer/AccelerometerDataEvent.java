package it.giuliatesta.udrive.accelerometer;

import java.io.Serializable;

import static it.giuliatesta.udrive.accelerometer.EventType.BOTH;
import static it.giuliatesta.udrive.accelerometer.EventType.DIRECTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.EventType.VERTICAL_MOTION_EVENT;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.NONE;

/**
    Classe che rappresenta l'evento di modifica che accelerazione
 */
public class AccelerometerDataEvent implements Serializable {

    private Direction direction;
    private int directionPercentage;
    private VerticalMotion verticalMotion;
    private int verticalMotionPercentage;
    private EventType type;
    private boolean isAStopEvent;

    /**
     * Costruttore vuoto
     */
    public AccelerometerDataEvent() {
    }

    /**
     * Costruttore di un evento completo, senza il valore del vettore accelerazione
     * @param direction tipo di direzione
     * @param directionPercentage   punteggio associato alla direzione
     * @param verticalMotion    tipo di movimento verticale
     * @param verticalMotionPercentage  punteggio associato al movimento
     */
    private AccelerometerDataEvent(Direction direction, int directionPercentage, VerticalMotion verticalMotion, int verticalMotionPercentage) {
        this.direction = direction;
        this.directionPercentage = directionPercentage;
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        this.type = BOTH;
        this.isAStopEvent=false;
    }

    /**
     * Metodo per la creazione di un evento di tipo BOTH
     * @param direction     tipo di direzione
     * @param directionPercentage   punteggio associato alla direzione
     * @param verticalMotion    tipo di movimento verticale
     * @param verticalMotionPercentage  punteggio associato al movimento verticale
     * @return nuova istanza dell'AccelerometerDataEvent di tipo BOTH
     */
    public static AccelerometerDataEvent createBothEvent(Direction direction, int directionPercentage, VerticalMotion verticalMotion, int verticalMotionPercentage) {
        return new AccelerometerDataEvent(direction, directionPercentage, verticalMotion, verticalMotionPercentage);
    }

    /**
     * Metodo per la creazione di un evento di tipo DIRECTION_EVENT
     * @param direction     tipo di direzione
     * @param directionPercentage   punteggio associato alla direzione
     * @return  nuova istanza dell'AccelerometerDataEvent di tipo DIRECTION_EVENT
     */
    public static AccelerometerDataEvent createDirectionEvent(Direction direction, int directionPercentage) {
        return new AccelerometerDataEvent(direction, directionPercentage);
    }

    /**
     * Metodo per la creazione di un evento di tipo VERTICAL_MOTION_EVENT
     * @param verticalMotion    tipo di movimento verticale
     * @param verticalMotionPercentage punteggio associato al movimento
     * @return nuova istanza dell'AccelerometerDataEvent di tipo VERTICAL_MOTION_EVENT
     */
    public static AccelerometerDataEvent createVerticalMotionEvent(VerticalMotion verticalMotion, int verticalMotionPercentage) {
        return new AccelerometerDataEvent(verticalMotion, verticalMotionPercentage);
    }

    /**
     * Costrutore per un evento di tipo DIRECTION_EVENT
     * @param direction tipo di direzione
     * @param directionPercentage punteggio associato alla direzione
     */
    private AccelerometerDataEvent(Direction direction, int directionPercentage) {
        this.direction = direction;
        this.directionPercentage = directionPercentage;
        this.verticalMotion = NONE;
        this.verticalMotionPercentage = 0;
        this.type = DIRECTION_EVENT;
        this.isAStopEvent=false;
    }

    /**
     * Costruttore per un evento di tipo VERTICAL_MOTION_EVENT
     * @param verticalMotion    tipo di movimento
     * @param verticalMotionPercentage  punteggio associato al movimento
     */
    private AccelerometerDataEvent(VerticalMotion verticalMotion, int verticalMotionPercentage) {
        this.verticalMotion = verticalMotion;
        this.verticalMotionPercentage = verticalMotionPercentage;
        this.direction = Direction.NONE;
        this.directionPercentage = 0;
        this.type = VERTICAL_MOTION_EVENT;
        this.isAStopEvent=false;
    }

    /**
     * Metodo get per la direzione indicata dall'accelerometro
     * Non dovrebbe mai accadere che viene richiesta la direction quando l'evento è ti tipo VERTICAL_MOTION_EVENT
     * @return direzione indicata dall'accelerometro
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Metodo get per il punteggio ottenuto della direzione
     * Non dovrebbe mai accadere che viene richiesta la directionPercentage quando l'evento è ti tipo VERTICAL_MOTION_EVENT
     * @return percentuale
     */
    public int getDirectionPercentage() {
        return directionPercentage;
    }

    /**
     * Metodo get per il punteggio ottenuto del movimento verticale
     * Non dovrebbe mai accadere che viene richiesta la verticalMotionPercentage quando l'evento è ti tipo DIRECTION_EVENT
     * @return percentuale
     */
    public int getVerticalMotionPercentage() {
        return verticalMotionPercentage;
    }

    /**
     * Metodo get per il tipo di movimento verticale
     * Non dovrebbe mai accadere che viene richiesta la verticalMotion quando l'evento è ti tipo DIRECTION_EVENT
     * @return movimento verticale
     */
    public VerticalMotion getVerticalMotion() {
            return verticalMotion;
    }

    /**
     * chiede se l'evento è un evento di STOP, in cui non c'è nessun movimento
     * @return  flag che indica se è un evento STOP
     */
    public boolean isAStopEvent() {
        return isAStopEvent;
    }

    /**
     * Imposta il tipo di evento
     * Se è un evento di tipo STOP il paramentro sarà TRUE, altrimenti FALSE
     * @param AStopEvent    TRUE se è un evento STOP
     */
    public void setAStopEvent(boolean AStopEvent) {
        isAStopEvent = AStopEvent;
    }

    /**
     * Metodo get per il tipo di evento
     * @return tipo di evento
     */
    public EventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AccelerometerDataEvent{" +
                "direction = " + direction +
                ", directionPercentage = " + directionPercentage +
                ", verticalMotion = " + verticalMotion +
                ", verticalMotionPercentage = " + verticalMotionPercentage +
                ", type = " + type +
                '}';
    }
}
