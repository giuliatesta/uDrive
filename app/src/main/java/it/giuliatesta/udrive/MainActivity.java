package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.WHITE;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Impostazioni per l'Activity
        Button btn_drive = findViewById(R.id.btn_guida);

        // Registrazione del listener al bottone
        btn_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(v);
            }
        });

        // impostazioni per l'immagine
        ImageView img_driving = findViewById(R.id.img_driving);
        img_driving.setColorFilter(WHITE);
    }

    // Metodo per cambiare activity e andare alla Drive
    public void changeActivity(View view) {
        Intent intent = new Intent(this, DriveActivity.class);
        startActivity(intent);
    }

    // Metodo per la creazione del menù
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // Metodo per gestire cosa viene selezionato nel menù
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                //change activity ---> activity_help
                return true;
            case R.id.settings:
                //change activity ---> activity_settings
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
