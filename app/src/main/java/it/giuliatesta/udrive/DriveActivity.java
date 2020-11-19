package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import static android.graphics.Color.BLUE;
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
        Log.d("DriveActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        // impostazioni per il dataProcessor
        dataManager = getInstance(this);
        dataManager.registerListener(this);

        // impostazioni per la listView
        percentageList = new ArrayList<String>();
        listView = findViewById(R.id.percentage_list);
        adapter = new ArrayAdapter<>(this, R.layout.activity_drive, percentageList);
        listView.setAdapter(adapter);
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

        // impostazioni per le immagini delle direzioni
        ImageView img_forward = findViewById(R.id.diction_forward);
        ImageView img_backward = findViewById(R.id.direction_backward);
        ImageView img_left = findViewById(R.id.direction_left);
        ImageView img_right = findViewById(R.id.direction_right);

        // DA SISTEMARE PERCHè è ORRIBILE
        switch (event.direction) {
            case FORWARD:
                // Se la direzione ricevuta dall'evento è FORWARD cambia colore alla freccia FORWARD
                img_forward.setColorFilter(BLUE);
                img_backward.clearColorFilter();
                img_left.clearColorFilter();
                img_right.clearColorFilter();
            case BACKWARD:
                img_backward.setColorFilter(BLUE);
                img_forward.clearColorFilter();
                img_left.clearColorFilter();
                img_right.clearColorFilter();
            case LEFT:
                img_left.setColorFilter(BLUE);
                img_backward.clearColorFilter();
                img_forward.clearColorFilter();
                img_right.clearColorFilter();
            case RIGHT:
                img_right.setColorFilter(BLUE);
                img_backward.clearColorFilter();
                img_forward.clearColorFilter();
                img_left.clearColorFilter();
        }

        percentageList.add(event.direction + ": " + event.percentage + "%");
        adapter.notifyDataSetChanged();
    }

}
