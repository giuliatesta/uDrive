package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.WHITE;

/**
 * Classe per la prima Activity --> da usare quando si avvia l'applicazione
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public void changeActivity(View view) {
        Intent intent = new Intent(this, DriveActivity.class);
        startActivity(intent);
    }
}