package com.example.agendaCorsi.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendaCorsi.ui.contatti.ModificaContatto;
import com.example.agendaCorsi.ui.contatti.NuovoContatto;
import com.example.agendaCorsi.ui.corsi.ElencoCorsi;
import com.example.agendaCorsi.ui.corsi.ModificaCorso;
import com.example.agendacorsi.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
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

    public Button annulla, esci, elimina, salva, chiudi, sospendi, apri, inserisci, ricarica5, ricarica10;
    public TableRow tableRow;

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

    public void listenerEsci(Context contextFrom, Class classDestination, Map<String, String> intentMap) {
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contextFrom, classDestination);
                if (intentMap != null) {
                    for (Map.Entry<String, String> entry : intentMap.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                startActivity(intent);
            }
        });
    }

    public void listenerAnnulla() {
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAnnulla();
            }
        });
    }

    public void listenerElimina() {
        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeElimina();
            }
        });
    }

    public void listenerInserisci(Context contextFrom, Class classDestination, Map<String, String> intentMap) {
        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contextFrom, classDestination);
                if (intentMap != null) {
                    for (Map.Entry<String, String> entry : intentMap.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                startActivity(intent);
            }
        });
    }

    public void listenerSalva() {
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSalva();
            }
        });
    }

    public void listenerChiudi() {
        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeChiudi();
            }
        });
    }

    public void listenerSospendi() {
        sospendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSospendi();
            }
        });
    }

    public void listenerApri() {
        apri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeApri();
            }
        });
    }

    public void listenerTableRow(Context contextFrom, Class classDestination, String idName, Map<String, String> intentMap) {
        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow tableRow = (TableRow) view;
                TextView textView = (TextView) tableRow.getChildAt(1);
                Integer idSelezionato = Integer.parseInt(textView.getText().toString());
                /**
                 * Passo alla classe destinazione l'id della riga selezionata più tutti gli item inseriti nella intentMap
                 */
                Intent intent = new Intent(contextFrom, classDestination);
                intent.putExtra(idName, idSelezionato);
                if (intentMap != null) {
                    for (Map.Entry<String, String> entry : intentMap.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                startActivity(intent);
            }
        });
    }

    public void listenerRicarica5() {
        ricarica5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRicarica(5);
            }
        });
    }

    public void listenerRicarica10() {
        ricarica10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRicarica(10);
            }
        });
    }

    public void updateLabel(EditText data, Calendar calendar){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        data.setText(dateFormat.format(calendar.getTime()));
    }

    public void makeAnnulla() {}

    public void makeElimina() {}

    public void makeSalva() {}

    public void makeApri() {}

    public void makeSospendi() {}

    public void makeChiudi() {}

    public void makeRicarica(int value) {}
}
