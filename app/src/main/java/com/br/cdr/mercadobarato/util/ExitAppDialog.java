package com.br.cdr.mercadobarato.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.content.DialogInterface;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by clebr on 21/02/2017.
 */

public class ExitAppDialog extends AppCompatDialogFragment implements DialogInterface.OnClickListener {
    private ExitListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ExitListener)) {
            throw new RuntimeException("A activity precisa implementar a interface ExitDialog. ExitListener");
        }

        listener = (ExitListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Deseja sair?")
                .setPositiveButton("Sim", this)
                .setNegativeButton("N\u00e3o", this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE && listener != null) {
            listener.onExit();
        }
    }

    public interface ExitListener {
        public void onExit();
    }

}
