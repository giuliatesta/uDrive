package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;
import it.giuliatesta.udrive.accelerometer.Direction;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static it.giuliatesta.udrive.DataManager.getInstance;

/*
 Classe per la seconda Activity --> da usare quando si avvia la guida
 */

public class DriveActivity extends AppCompatActivity implements AccelerometerDataEventListener {

    private DataManager dataManager;
    private Button btn_stop;
    private ArrayList<String> percentageList;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        // impostazioni per il dataProcessor
        dataManager = getInstance(this);
        dataManager.registerListener(this);

        // impostazioni per la listView
        /*percentageList = new ArrayList<String>();
        listView = findViewById(R.id.percentage_list);
        adapter = new ArrayAdapter<>(this, R.layout.activity_drive, percentageList);
        listView.setAdapter(adapter);*/

        // Impostazioni per le immagini
        imageSettings();
    }



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

    // Metodo per cambiare activity e andare alla Results
    public void changeActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }


    // Metodo per la creazione del menù
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_drive, menu);
        return true;
    }

    // Metodo per gestire cosa viene selezionato nel menù
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                changeActivity(MainActivity.class);
                return true;
            case R.id.stop:
                changeActivity(ResultsActivity.class);
                return true;
            case R.id.help:
                // Magari aggiungere una pagina di aiuto pagina web? nuova activity? da definire
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDataChanged(AccelerometerDataEvent event) {

        ImageView img_forward = findViewById(R.id.img_diction_forward);
        ImageView img_backward = findViewById(R.id.img_direction_backward);
        ImageView img_left = findViewById(R.id.img_direction_left);
        ImageView img_right = findViewById(R.id.img_direction_right);

        setDirectionBlack(img_forward);
        setDirectionBlack(img_backward);
        setDirectionBlack(img_left);
        setDirectionBlack(img_right);

        switch (event.direction) {
            case FORWARD:
                // Se la direzione ricevuta dall'evento è FORWARD cambia colore alla freccia FORWARD
                setDirectionBlue(img_forward);
                break;
            case BACKWARD:
                setDirectionBlue(img_backward);
                break;
            case LEFT:
                setDirectionBlue(img_left);
                break;
            case RIGHT:
                setDirectionBlue(img_right);
                break;
        }

        setPercentageList(event.percentage, event.direction);
    }

    /*
            TO DO: bisogna aggiungere le immagini e i colori di background
     */
    private void setPercentageList(int percentage, Direction direction) {
         /*percentageList.add(event.direction + ": " + event.percentage + "%");
        adapter.notifyDataSetChanged();*/
        int backgroundColor = getBackgroundColorForPercentageList(percentage);
        //ImageView imageDirection = getImageByDirection(direction);
    }



    private int getBackgroundColorForPercentageList(int percentage) {
        if(percentage > 75) {
            return GREEN;
        } else if (percentage < 25) {
            return RED;
        } else {
            return YELLOW;
        }
    }
    /*
        Imposta un filtro colore sul una immagine in modo da evidenziarla
     */
    private void setDirectionBlue(ImageView image) {
        image.setColorFilter(BLUE);
    }

    /*
        Elimina il filtro colore facendo tornare l'immagine nera
     */
    private void setDirectionBlack(ImageView image) {
        image.clearColorFilter();
    }


}
