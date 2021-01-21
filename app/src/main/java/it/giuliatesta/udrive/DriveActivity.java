package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
import it.giuliatesta.udrive.accelerometer.Direction;
import it.giuliatesta.udrive.accelerometer.VerticalMotion;
import it.giuliatesta.udrive.dataProcessing.DataManager;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;
import static it.giuliatesta.udrive.dataProcessing.DataManager.getInstance;
import static it.giuliatesta.udrive.R.id.percentage_list_view;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.POTHOLE;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.ROADBUMP;

/**
 Classe per la seconda Activity --> da usare quando si avvia la guida
 */

public class DriveActivity extends AppCompatActivity implements AccelerometerDataEventListener {
    private DataManager dataManager;
    private ArrayList<AccelerometerDataEvent> accelerometerEventList;
    private ImageView img_forward, img_backward, img_left, img_right, img_vertical_motion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        accelerometerEventList = new ArrayList<>();

        // Impostazioni per il dataManager: ottengo la sua istanza e registro il listener
        dataManager = getInstance(this);
        dataManager.registerListener(this);

        // Impostazioni per le immagini di decorazione
        decorationImageSettings();

        // Impostazioni per le immagini delle direzioni
        directionAndVerticalMotionImageSettings();
    }

    /**
     * Impostazioni per le immagini: associo le immagini e imposto un filtro colore
     */
    private void decorationImageSettings() {
        ImageView steering_wheel = findViewById(R.id.img_steering_wheel);
        setImageBlue(steering_wheel);

        ImageView racing_cars = findViewById(R.id.img_racing_cars);
        setImageBlue(racing_cars);

        ImageView car_road = findViewById(R.id.img_car_road);
        setImageBlue(car_road);

        ImageView traffic = findViewById(R.id.img_traffic);
        setImageBlue(traffic);
    }

    /**
     * Associo le immagini legate alle 4 direzioni e applico un filtro colore
     */
    private void directionAndVerticalMotionImageSettings() {
        img_forward = findViewById(R.id.img_diction_forward);
        img_backward = findViewById(R.id.img_direction_backward);
        img_left = findViewById(R.id.img_direction_left);
        img_right = findViewById(R.id.img_direction_right);
        img_vertical_motion = findViewById(R.id.img_road_bump);
        setAllImagesWhite();
    }

    /**
     *  Metodo per cambiare activity e andare alla Results
     */
    public void changeActivity() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("percentageList", accelerometerEventList);
        startActivity(intent);
    }

    /**
     *   Metodo per la creazione del menù
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_drive, menu);
        return true;
    }

    /**
     * Metodo per gestire cosa viene selezionato nel menù
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop:
                changeActivity();
                dataManager.getStorageListener().stopWritingStorageFile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChanged(AccelerometerDataEvent event) {
        // In base al tipo di evento, imposto diverse visualizzazioni
        if(event.isAStopEvent() == false) {
            listViewSettings(event);
            switch (event.getType()) {
                case DIRECTION_EVENT:
                    setDirection(event.getDirection());
                    break;
                case VERTICAL_MOTION_EVENT:
                    setVerticalMotion(event.getVerticalMotion());
                    break;
                case BOTH:
                    setDirection(event.getDirection());
                    setVerticalMotion(event.getVerticalMotion());
                    break;
            }
        }
    }

    /**
     * In base al tipo di movimento verticale, imposto un filtro colore all'immagine associata
     * @param verticalMotion movimento verticale
     */
    private void setVerticalMotion(VerticalMotion verticalMotion) {
        if(verticalMotion == POTHOLE || verticalMotion == ROADBUMP) {
            setImageBlue(img_vertical_motion);
        } else {
            setAllImagesWhite();
        }
    }

    /**
     * In base alla direzione, imposto un filtro colore all'immagine associata
     * @param direction direzione dell'evento arrivato
     */
    private void setDirection(Direction direction) {
        setAllImagesWhite();
        switch (direction) {
        case FORWARD:
            setImageBlue(img_forward);
            break;
        case BACKWARD:
            setImageBlue(img_backward);
            break;
        case LEFT:
            setImageBlue(img_left);
            break;
        case RIGHT:
            setImageBlue(img_right);
            break;
        default:
            setAllImagesWhite();
        }
    }

    private void setAllImagesWhite() {
        setImageWhite(img_forward);
        setImageWhite(img_backward);
        setImageWhite(img_left);
        setImageWhite(img_right);
        setImageWhite(img_vertical_motion);
    }

    /**
     * Impostazioni per la listView: aggiungo le immagini all'array e imposto l'adapter
     */
    private void listViewSettings(AccelerometerDataEvent event) {
        accelerometerEventList.add(0, event);
        CustomAdapter adapter = new CustomAdapter(DriveActivity.this, accelerometerEventList);
        ListView listView = findViewById(percentage_list_view);
        listView.setAdapter(adapter);
    }

    /**
        Imposta un filtro colore sul una immagine in modo da evidenziarla
        @param image immagine a cui si vuole applicare il filtro colore
     */
    private void setImageBlue(ImageView image) {
        image.setColorFilter(BLUE);
    }

    /**
        Elimina il filtro colore facendo tornare l'immagine nera
        @param image immagine a cui si vuole togliere il filtro colore
     */
    private void setImageWhite(ImageView image) {
        image.setColorFilter(WHITE);
    }
}
