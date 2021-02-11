package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private boolean exit = false;

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
     * @param view view
     */
    private void changeActivity(View view) {
        Intent intent = new Intent(this, DriveActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Tenere il dispositivo in verticale", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.resetStorage) {
            ResetStatus status = dataManager.getStorageListener().resetStorageFile();
            makeFeedbackToast(status);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Crea un toast per notificare all'utente lo stato dell'operazione
     * @param status    stato dell'operazione
     */
    private void makeFeedbackToast(ResetStatus status) {
        if (status == SUCCESS) {
            // Se è stato un successo
            Toast.makeText(this, "File di archivio resettato", Toast.LENGTH_SHORT).show();
        } else {
            // Se è stato un fallimento
            Toast.makeText(this, "ERRORE! Impossibile resettare il file di archivio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        if (exit) {
            finishAffinity();
        }
        else {
            Toast.makeText(this, "Premi nuovamente per uscire",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            }, 1000);
        }}
}