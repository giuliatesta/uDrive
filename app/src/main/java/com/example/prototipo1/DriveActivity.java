package com.example.prototipo1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.makeText;

/*
 Classe per la seconda Activity --> da usare quando si avvia la guida
 */

public class DriveActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "DriveActivity";
    private SensorManager manager;
    public Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        // impostazioni per il sensore
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

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

    // Quando l'activity viene cambiata, viene tolto il listener
    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView text_x = findViewById(R.id.text_x);
        TextView text_y = findViewById(R.id.text_y);
        TextView text_z = findViewById(R.id.text_z);

        if(event.sensor.getType()==accelerometer.getType()) {       //Se gli eventi sono dell'accelerometro
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            text_x.setText("X: " + x );
            text_y.setText("Y: " + y );
            text_z.setText("Z: " + x );


        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
