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

import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;
import static it.giuliatesta.udrive.DataManager.getInstance;
import static it.giuliatesta.udrive.R.id.percentage_list_view;
import static it.giuliatesta.udrive.accelerometer.Direction.DEFAULT;

/**
 Classe per la seconda Activity --> da usare quando si avvia la guida
 */

public class DriveActivity extends AppCompatActivity implements AccelerometerDataEventListener {

    private DataManager dataManager;
    private ArrayList<AccelerometerDataEvent> accelerometerEventList;
    private ListView listView;
    private Integer[] imageId;
    private CustomAdapter adapter;
    private ImageView img_forward, img_backward, img_left, img_right, img_road_bump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        accelerometerEventList = new ArrayList<>();

        // Impostazioni per il dataManager
        dataManagerSettings();

        // Impostazioni per le immagini di decorazione
        decorationImageSettings();

        // Impostazioni per le immagini delle direzioni
        directionAndVerticalMotionImageSettings();
    }

    /**
     * Impostazioni per il dataManager: ottengo la sua istanza e registro il listener
     */
    private void dataManagerSettings() {
        dataManager = getInstance(this);
        dataManager.registerListener(this);
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
        img_road_bump = findViewById(R.id.img_road_bump);

        setImageWhite(img_forward);
        setImageWhite(img_backward);
        setImageWhite(img_left);
        setImageWhite(img_right);
        setImageWhite(img_road_bump);
    }

    /**
     *  Metodo per cambiare activity e andare alla Results
     */
    public void changeActivity(Class cls) {
        Intent intent = new Intent(this, cls);
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
            case R.id.home:
                changeActivity(MainActivity.class);
                return true;
            case R.id.stop:
                changeActivity(ResultsActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChanged(AccelerometerDataEvent event) {
        img_road_bump.setColorFilter(WHITE);
        setImageWhite(img_forward);
        setImageWhite(img_backward);
        setImageWhite(img_left);
        setImageWhite(img_right);
        setImageWhite(img_road_bump);

        // Per la direzione
        setDirection(event.getDirection());

        // Per il movimento verticale
        setVerticalMotion(event.getVerticalMotion());

        // Per la percentuale
        listViewSettings(event);

        System.out.println(event.toString());
    }

    private void setVerticalMotion(VerticalMotion verticalMotion) {
        switch (verticalMotion) {
            case POTHOLE:
                setImageBlue(img_road_bump);
                break;
            case ROADBUMP:
                setImageBlue(img_road_bump);
                break;
            case NONE:
                setImageBlue(img_road_bump);
                break;
        }
    }

    private void setDirection(Direction direction) {
        switch (direction) {
        case FORWARD:
            // Se la direzione ricevuta dall'evento è FORWARD cambia colore alla freccia FORWARD
            setImageBlue(img_forward);
            break;
        case BACKWARD:
            setImageBlue(img_backward);
            break;
        case LEFT:
            setImageBlue(img_left);
            break;
        case RIGHT:
            setImageWhite(img_right);
            break;
        }
    }

    /**
     * Impostazioni per la listView: aggiungo le immagini all'array e imposto l'adapter
     */
    private void listViewSettings(AccelerometerDataEvent event) {
        if (event.getDirection() != DEFAULT) {
            accelerometerEventList.add(0, event);
        }
        adapter = new CustomAdapter(DriveActivity.this, accelerometerEventList);
        listView = findViewById(percentage_list_view);
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
