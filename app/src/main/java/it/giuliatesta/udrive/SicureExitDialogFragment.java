package it.giuliatesta.udrive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Classe per la creazione del pop up di uscita sicura
 */
public class SicureExitDialogFragment extends DialogFragment {

    private Activity activity;

    /**
     * Costruttore
     * @param activity activity da far ripartire quando viene premuto sì nel popup
     */
    public SicureExitDialogFragment(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialogFragment);

        // Imposto il messaggio da mostrare
        builder.setMessage(R.string.popup)
                // Imposto un bottone per il no
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                // Imposto un bottone per il sì
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, MainActivity.class);
                        getContext().startActivity(intent);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return builder.create();
    }
}