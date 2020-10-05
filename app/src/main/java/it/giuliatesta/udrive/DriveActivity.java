package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.AccelerometerDataEventListener;

import static it.giuliatesta.udrive.DataManager.getInstance;

/*
 Classe per la seconda Activity --> da usare quando si avvia la guida
 */

public class DriveActivity extends AppCompatActivity implements AccelerometerDataEventListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        // impostazioni per il dataProcessor
        DataManager dataManager = getInstance();
        dataManager.registerListener(this);

        // impostazioni dell'activity
        Button btn_stop = findViewById(R.id.btn_stop);

        // Registrazione del listener per il bottone
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(v);
            }
        });
    }

    // Metodo per cambiare activity e andare alla Results
    public void changeActivity(View view) {
        Intent intent = new Intent(this, ResultsActivity.class);
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                //change activity ---> activity_settings
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChanged(AccelerometerDataEvent event) {
        TextView text_x = findViewById(R.id.text_x);
        TextView text_y = findViewById(R.id.text_y);
        TextView text_z = findViewById(R.id.text_z);
        text_x.setText("Direction: " + event.direction);
        text_y.setText("Acceleration: " + event.acceleration);
        text_z.setText("Perc: " + event.percentage + "%");
    }
}
