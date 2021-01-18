package it.giuliatesta.udrive.dataProcessing;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;

public class StorageListener implements AccelerometerDataEventListener {

    private final Context context;
    private File storageFile;

    public StorageListener(Context context) {
        this.context = context;
        storageFile = createFile();
    }

    private File createFile() {
        return new File(context.getFilesDir(), "storageFile.txt");
    }

    private void writeFile(AccelerometerDataEvent event) {
        try (FileWriter writer = new FileWriter(storageFile,true)){
            String line = createLine(event, writer);
            writer.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createLine(AccelerometerDataEvent event, FileWriter writer) {
        String operationType = "";
        String percentage = "";
        String line = "";
        switch (event.getType()) {
            case DIRECTION_EVENT:
                operationType = event.getDirection().name();
                percentage = String.valueOf(event.getDirectionPercentage());
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
        }
        return line;
    }

    private String createLineFromDirectionAndVerticalMotionEvent(String direction, String directionPercentage, String verticalMotion, String verticalMotionPercentage) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String firstLine = String.valueOf(timestamp) + "\t" + direction + "\t" + directionPercentage + "\n";
        String secondLine = String.valueOf(timestamp) + "\t" + verticalMotion + "\t" + verticalMotionPercentage + "\n";
        return (firstLine + secondLine);
    }

    private String createLineFromDirectionOrVerticalMotionEvent(String operationType, String percentage) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp) + "\t" + operationType + "\t" + percentage + "\n";

    }

    @Override
    public void onDataChanged(AccelerometerDataEvent event) {
        if(!event.isAStopEvent()) {
            writeFile(event);
        }
    }
}
