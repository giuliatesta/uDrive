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
    private ImageView img_forward, img_backward, img_left, img_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        accelerometerEventList = new ArrayList<>();

        // Impostazioni per il dataManager
        dataManagerSettings();

        // Impostazioni per le immagini di decorazione
        imageSettings();

        // Impostazioni per le immagini delle direzioni
        directionImageSettings();

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
    private void imageSettings() {

        ImageView steering_wheel = findViewById(R.id.img_steering_wheel);
        steering_wheel.setColorFilter(BLUE);

        ImageView racing_cars = findViewById(R.id.img_racing_cars);
        racing_cars.setColorFilter(BLUE);

        ImageView car_road = findViewById(R.id.img_car_road);
        car_road.setColorFilter(BLUE);

        ImageView traffic = findViewById(R.id.img_traffic);
        traffic.setColorFilter(BLUE);
    }

    /**
     * Associo le immagini legate alle 4 direzioni e applico un filtro colore
     */
    private void directionImageSettings() {
        img_forward = findViewById(R.id.img_diction_forward);
        img_backward = findViewById(R.id.img_direction_backward);
        img_left = findViewById(R.id.img_direction_left);
        img_right = findViewById(R.id.img_direction_right);

        setDirectionImageWhite(img_forward);
        setDirectionImageWhite(img_backward);
        setDirectionImageWhite(img_left);
        setDirectionImageWhite(img_right);
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

        setDirectionImageWhite(img_forward);
        setDirectionImageWhite(img_backward);
        setDirectionImageWhite(img_left);
        setDirectionImageWhite(img_right);

        switch (event.getDirection()) {
            case FORWARD:
                // Se la direzione ricevuta dall'evento è FORWARD cambia colore alla freccia FORWARD
                setDirectionImageBlue(img_forward);
                break;
            case BACKWARD:
                setDirectionImageBlue(img_backward);
                break;
            case LEFT:
                setDirectionImageBlue(img_left);
                break;
            case RIGHT:
                setDirectionImageBlue(img_right);
                break;
        }

        // Per la percentuale
        // Non iserirsco i valori di default perchè non ho cambiato direzione
        // e quindi non mi interessa il punteggio
        if (event.getDirection() != DEFAULT) {
            accelerometerEventList.add(0, event);
        }
        listViewSettings();
    }

    /**
     * Impostazioni per la listView: aggiungo le immagini all'array e imposto l'adapter
     */
    private void listViewSettings() {
        adapter = new CustomAdapter(DriveActivity.this, accelerometerEventList);
        listView = findViewById(percentage_list_view);
        listView.setAdapter(adapter);
    }

    /**
        Imposta un filtro colore sul una immagine in modo da evidenziarla
        @param image immagine a cui si vuole applicare il filtro colore
     */
    private void setDirectionImageBlue(ImageView image) {
        image.setColorFilter(BLUE);
    }

    /**
        Elimina il filtro colore facendo tornare l'immagine nera
        @param image immagine a cui si vuole togliere il filtro colore
     */
    private void setDirectionImageWhite(ImageView image) {
        image.setColorFilter(WHITE);
    }


}
