package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.WHITE;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Impostazioni dell'activity
        Button btn_home = findViewById(R.id.btn_home);

        // Registrazione del listener al bottone
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(v);
            }
        });

        //Impostazioni dell'immagine
        ImageView img_thumbs_up = findViewById(R.id.img_thumbs_up);
        img_thumbs_up.setColorFilter(WHITE);

    }

    // Metodo per cambiare activity e tornare alla Main
    public void changeActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

