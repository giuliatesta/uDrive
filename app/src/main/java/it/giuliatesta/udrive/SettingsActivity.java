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
        SeekBar seekBarMax = findViewById(R.id.seek_bar_max);

        double minValueX = Configuration.INSTANCE.getMinValueX();
        double minValueY = Configuration.INSTANCE.getMinValueY();
        double minValueZ = Configuration.INSTANCE.getMinValueZ();
        double maxValue = Configuration.INSTANCE.getMaxValue();

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
                double newMinValue = Configuration.INSTANCE.calculateProportionForMinValue(progress);
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
                double newMinValue = Configuration.INSTANCE.calculateProportionForMinValue(progress);
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
                double newMinValue = Configuration.INSTANCE.calculateProportionForMinValue(progress);
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

        OnSeekBarChangeListener seekBarMaxChangeListener = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double newMaxValue = Configuration.INSTANCE.calculateProportionForMaxValue(progress);
                textViewMax.setText(String.valueOf(newMaxValue));
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


    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startMainActivity();
    }
}
