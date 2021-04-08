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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekBarSettings();
    }

    private void seekBarSettings() {
        SeekBar seekBarX = findViewById(R.id.seek_bar_x);
        SeekBar seekBarY = findViewById(R.id.seek_bar_y);
        SeekBar seekBarZ = findViewById(R.id.seek_bar_z);

        double minValueX = Configuration.INSTANCE.getMinValueX();
        double minValueY = Configuration.INSTANCE.getMinValueY();
        double minValueZ = Configuration.INSTANCE.getMinValueZ();

        final TextView textViewX = findViewById(R.id.x_value);
        final TextView textViewY = findViewById(R.id.y_value);
        final TextView textViewZ = findViewById(R.id.z_value);

        textViewX.setText(""+ minValueX);
        textViewY.setText(""+ minValueY);
        textViewZ.setText(""+ minValueZ);

        // Listener per il seek bar della componente x
        OnSeekBarChangeListener seekBarXChangeListener = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double newMinValue = Configuration.INSTANCE.calculateProportion(progress);
                textViewX.setText("" + newMinValue);
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
                double newMinValue = Configuration.INSTANCE.calculateProportion(progress);
                textViewY.setText("" + newMinValue);
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
                double newMinValue = Configuration.INSTANCE.calculateProportion(progress);
                textViewZ.setText("" + newMinValue);
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

    }


    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startMainActivity();
    }
}
