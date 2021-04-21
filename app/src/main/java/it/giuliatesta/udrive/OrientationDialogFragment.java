package it.giuliatesta.udrive;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import it.giuliatesta.udrive.dataProcessing.DataManager;

import static it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent.DeviceOrientation.HORIZONTAL;
import static it.giuliatesta.udrive.accelerometer.CoordinatesDataEvent.DeviceOrientation.VERTICAL;

/**
 * Classe per creare il pop up necessario per la richiesta di orientamento verticale o orizzontale
 */
public class OrientationDialogFragment extends DialogFragment {

    private Activity activity;

    OrientationDialogFragment(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity(), R.style.dialogFragment);

        builder.setMessage("Scegli l'orientamento del dispositivo")
                .setPositiveButton("Verticale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, DriveActivity.class);
                        getContext().startActivity(intent);
                        DataManager.setDeviceOrientation(VERTICAL);

                    }
                })
                .setNegativeButton("Orizzontale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, DriveActivity.class);
                        getContext().startActivity(intent);
                        DataManager.setDeviceOrientation(HORIZONTAL);
                    }
                });

        return builder.create();
    }


}
