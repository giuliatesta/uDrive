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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import it.giuliatesta.udrive.dataProcessing.DataManager;
import it.giuliatesta.udrive.dataProcessing.StorageListener.ResetStatus;

import static android.graphics.Color.WHITE;
import static it.giuliatesta.udrive.dataProcessing.DataManager.getInstance;
import static it.giuliatesta.udrive.dataProcessing.StorageListener.ResetStatus.SUCCESS;

/**
 * Classe per la prima Activity --> da usare quando si avvia l'applicazione
 */
public class MainActivity extends AppCompatActivity {
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = getInstance(this);
        // Impostazioni per il listener
        listenerSettings();

        // Impostazioni per l'immagine
        imageSettings();
    }

    /**
     * Metodo per le impostazioni delle immagini: associo la imageView di MainActivity.java
     * con la imageView di activity_main.xml e applico un filtro colore
     */
    private void imageSettings() {
        ImageView img_driving = findViewById(R.id.img_driving);
        img_driving.setColorFilter(WHITE);
    }

    /**
     * Metodo per le impostazioni del listener: associo il bottone e
     * registro il listener quando viene schiacciato il bottone dall'utente
     */
    private void listenerSettings() {
        Button btn_drive = findViewById(R.id.btn_guida);
        btn_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(v);
            }
        });
    }

    /**
     * Metodo per cambiare l'activity
     *
     * @param view view
     */
    public void changeActivity(View view) {
        Intent intent = new Intent(this, DriveActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.resetStorage:
                Log.d("MainActivity", "onOptionsItemSelected: RESET STORAGE FILE");
                ResetStatus status = dataManager.getStorageListener().resetStorageFile();
                makeFeedbackToast(status);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeFeedbackToast(ResetStatus status) {
        if (status == SUCCESS) {
            Toast.makeText(this, "Reset storage file successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "ERROR! Reset storage file impossible!", Toast.LENGTH_SHORT).show();
        }
    }
}