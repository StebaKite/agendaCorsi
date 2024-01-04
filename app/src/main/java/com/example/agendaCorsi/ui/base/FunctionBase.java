package com.example.agendaCorsi.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.ui.contatti.ModificaContatto;
import com.example.agendacorsi.R;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

public class FunctionBase extends AppCompatActivity {

    public static String Skate = "SKATE";
    public static String Basket = "BASKET";
    public static String Pallavolo = "PALLAVOLO";
    public static String Pattini = "PATTINI";

    public static String STATO_APERTO = "Aperto";
    public static String STATO_CHIUSO = "Chiuso";
    public static String STATO_ATTIVO = "Attivo";
    public static String STATO_SOSPESO = "Sospeso";

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

    public String dateFormat(String date, String patternFrom, String patternTo) {
        try {
            Date dateParse = new SimpleDateFormat(patternFrom).parse(date);
            return new SimpleDateFormat(patternTo).format(dateParse);
        }
        catch (ParseException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            return "";
        }

    }

}
