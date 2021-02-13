package it.giuliatesta.udrive.dataProcessing;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;

import static it.giuliatesta.udrive.dataProcessing.StorageListener.ResetStatus.FAILURE;
import static it.giuliatesta.udrive.dataProcessing.StorageListener.ResetStatus.SUCCESS;

/**
 * Classe listener per la gestione del file di archivio degli eventi ricevuti
 */
public class StorageListener implements AccelerometerDataEventListener {

    private final Context context;
    private final File storageFile;


    /**
     * Enum per indicare se l'operazione di reset del file di archivio è stata un successo o un fallimento
     */
    public enum ResetStatus {
        /**
         * SUCCESS: se l'operazione è avvenuta correttamente
         * FAILURE: se l'operazione non è andata a buon fine
         */
        SUCCESS, FAILURE
    }

    /**
     * Costruttore
     * @param context   context
     */
    public StorageListener(Context context) {
        this.context = context;
        storageFile = createFile();
        writeLine(createHeadLine(), storageFile);
    }

    /**
     * Scrive la riga passata come parametro nel file passato come secondo parametro
     * @param line  stringa da aggiungere alla fine del file
     * @param storageFile   file in cui scrivere la riga
     */
    private void writeLine(String line, File storageFile) {
        try (FileWriter writer = new FileWriter(storageFile, true)) {
            writer.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Crea il file su cui scrivere l'archivio di eventi
     * Se il file è già presente non ne crea uno nuovo ma resistuisce quello già presente, altrimenti lo crea
     * @return  file su cui scrivere
     */
    private File createFile() {
        String path = context.getFilesDir().getPath();
        File file = new File(path, "storageFile.txt");
        return file;
    }

    /**
     * Crea la stringa da aggiungere all'inizio di una nuova guida
     * @return  stringa di introduzione di una nuova guida
     */
    private String createHeadLine() {
        return "\n---STARTED A NEW DRIVING---\n";
    }

    /**
     * Crea la stringa da aggiungere alla fine della guida
     * @return  stringa di fine guida
     */
    private String createEndLine() {
        return "---FINISHED DRIVING--- \n\n";
    }

    /**
     * Costruisce la linea da inserire nel file, prendendo le informazioni dall'evento arrivato
     * @param event     evento da cui prendere le informazioni
     * @return  la riga da aggiungere
     */
    private String createLine(AccelerometerDataEvent event) {
        String line = "";
        switch (event.getType()) {
            case DIRECTION_EVENT:
                String operationType = event.getDirection().name();
                String percentage = String.valueOf(event.getDirectionPercentage());
                line = createLineFromDirectionOrVerticalMotionEvent(operationType, percentage);
                break;
            case VERTICAL_MOTION_EVENT:
                operationType = event.getVerticalMotion().name();
                percentage = String.valueOf(event.getVerticalMotionPercentage());
                line = createLineFromDirectionOrVerticalMotionEvent(operationType, percentage);
                break;
            case BOTH:
                String direction = event.getDirection().name();
                String verticalMotion = event.getVerticalMotion().name();
                String directionPercentage = String.valueOf(event.getDirectionPercentage());
                String verticalMotionPercentage = String.valueOf(event.getVerticalMotionPercentage());
                line = createLineFromDirectionAndVerticalMotionEvent(direction, directionPercentage, verticalMotion, verticalMotionPercentage);
                break;
            default:
        }
        return line;
    }

    /**
     * Crea una stringa con le informazioni inserite come parametri per gli eventi di tipo BOTH
     * @param direction     tipo di direzione
     * @param directionPercentage   punteggio ottenuto dal movimento in quella direzione
     * @param verticalMotion    tipo di movimento verticale
     * @param verticalMotionPercentage  punteggio ottenuto dal movimento verticale
     * @return  linea da aggiungere al file
     */
    private String createLineFromDirectionAndVerticalMotionEvent(String direction, String directionPercentage, String verticalMotion, String verticalMotionPercentage) {
        String firstLine = createLineFromDirectionOrVerticalMotionEvent(direction, directionPercentage);
        String secondLine = createLineFromDirectionOrVerticalMotionEvent(verticalMotion, verticalMotionPercentage);
        return (firstLine + secondLine);
    }

    /**
     * Crea una stringa per gli eventi di tipo DIRECTION_EVENT o VERTICAL_MOTION_EVENT
     * @param operationType tipo di movimento
     * @param percentage    punteggio ottenuto
     * @return  riga da aggiungere al file
     */
    private String createLineFromDirectionOrVerticalMotionEvent(String operationType, String percentage) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp + "\t" + operationType + "\t" + percentage + "\n";

    }

    @Override
    public void onDataChanged(AccelerometerDataEvent event) {
        if(!event.isAStopEvent()) {
            writeLine(createLine(event), storageFile);
        }
    }

    /**
     * Scrive la riga finale quando viene chiamato perchè non ci sono piu eventi per quella particolare guida
     */
    public void stopWritingStorageFile() {
        writeLine(createEndLine(), storageFile);
    }

    /**
     * Resetta il file storageFile, che contiene gli eventi delle guide precedenti
     * @return stato dell'operazione SUCCESS se è stata fatta, FAILURE se non è andata a buon fine
     */
    public ResetStatus resetStorageFile() {
        try (FileWriter writer = new FileWriter(storageFile,true)){
            // Cancello il precedente e ne creo uno nuovo vuoto
           if(storageFile.delete() && storageFile.createNewFile()) {
               return SUCCESS;
           } else {
               return FAILURE;
           }
        } catch (IOException e) {
            e.printStackTrace();
            return FAILURE;

        }
    }
}