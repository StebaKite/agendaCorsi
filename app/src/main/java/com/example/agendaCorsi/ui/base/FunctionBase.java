package com.example.agendaCorsi.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agendaCorsi.ui.contatti.ModificaContatto;
import com.example.agendacorsi.R;

public class FunctionBase extends AppCompatActivity {

    public void displayAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(context, R.style.Theme_AlertDialog);
        messaggio.setTitle(title);
        messaggio.setMessage(message);
        messaggio.setCancelable(false);
        messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog ad = messaggio.create();
        ad.show();
    }

}
