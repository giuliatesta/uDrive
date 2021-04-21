package it.giuliatesta.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import it.giuliatesta.udrive.dataProcessing.Configuration;

public class SettingsActivity extends AppCompatActivity {

    // valori delle soglie minime e massima
    private double minValueX = Configuration.INSTANCE.getMinValueX();
    private double minValueY = Configuration.INSTANCE.getMinValueY();
    private double minValueZ = Configuration.INSTANCE.getMinValueZ();
    private double maxValue = Configuration.INSTANCE.getMaxValue();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekBarSettings();
    }

    /**
     * Metodo per la gestione delle seekBar
     */
    private void seekBarSettings() {
        SeekBar seekBarX = findViewById(R.id.seek_bar_x);
        SeekBar seekBarY = findViewById(R.id.seek_bar_y);
        SeekBar seekBarZ = findViewById(R.id.seek_bar_z);
        SeekBar seekBarMax = findViewById(R.id.seek_bar_max);

        // TextView dove scrivere il nuovo valore della soglia
        final TextView textViewX = findViewById(R.id.x_value);
        final TextView textViewY = findViewById(R.id.y_value);
        final TextView textViewZ = findViewById(R.id.z_value);
        final TextView textViewMax = findViewById(R.id.max_value);

        textViewX.setText(String.valueOf(minValueX));
        textViewY.setText(String.valueOf(minValueY));
        textViewZ.setText(String.valueOf(minValueZ));
        textViewMax.setText(String.valueOf(maxValue));

        // Listener per il seek bar della componente x
        OnSeekBarChangeListener seekBarXChangeListener = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Calcola il nuovo valore della soglia
                double newMinValue = Configuration.INSTANCE.calculateProportionForMinValue(progress);
                // Lo mostra nella textView
                textViewX.setText(String.valueOf(newMinValue));
                // E lo salva
                Configuration.INSTANCE.setMinValueX(newMinValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        seekBarX.setOnSeekBarChangeListener(seekBarXChangeListener);

        // Listener per il seek bar della componente y
        OnSeekBarChangeListener seekBarYChangeListener = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calcola il nuovo valore della soglia
                double newMinValue = Configuration.INSTANCE.calculateProportionForMinValue(progress);
                // Lo mostra nella textView
                textViewY.setText(String.valueOf(newMinValue));
                // E lo salva
                Configuration.INSTANCE.setMinValueY(newMinValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        seekBarY.setOnSeekBarChangeListener(seekBarYChangeListener);

        // Listener per il seek bar della componente z
        OnSeekBarChangeListener seekBarZChangeListener = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calcola il nuovo valore della soglia
                double newMinValue = Configuration.INSTANCE.calculateProportionForMinValue(progress);
                // Lo mostra nella textView
                textViewZ.setText(String.valueOf(newMinValue));
                // E lo salva
                Configuration.INSTANCE.setMinValueZ(newMinValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        seekBarZ.setOnSeekBarChangeListener(seekBarZChangeListener);

        // Listener per il seek bar della soglia massima
        OnSeekBarChangeListener seekBarMaxChangeListener = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calcola la nuova soglia massima
                double newMaxValue = Configuration.INSTANCE.calculateProportionForMaxValue(progress);
                // La mostra nella textView
                textViewMax.setText(String.valueOf(newMaxValue));
                // E la salva
                Configuration.INSTANCE.setMaxValue(newMaxValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBarMax.setOnSeekBarChangeListener(seekBarMaxChangeListener);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
