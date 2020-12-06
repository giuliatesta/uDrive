package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.WHITE;

/**
 * Classe per la terza Activity --> da usare quando si termina la guida
 */
public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Impostazioni del listener
        listenerSettings();

        //Impostazioni dell'immagine
        imageSettings();
    }

    /**
     * Metodo per le impostazioni dell'immagine: associo l'immagine e le applico un filtro colore
     *
     */
    private void imageSettings() {
        ImageView img_thumbs_up = findViewById(R.id.img_thumbs_up);
        img_thumbs_up.setColorFilter(WHITE);
    }

    /**
     * Metodo per le impostazioni dell listener: associo il bottone e registro il listener
     */
    private void listenerSettings() {
        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(v);
            }
        });
    }

    /**
     *  Metodo per cambiare activity e tornare alla Main
     */
    public void changeActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

