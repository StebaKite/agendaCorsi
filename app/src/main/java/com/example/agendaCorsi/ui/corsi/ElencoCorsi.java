package com.example.agendaCorsi.ui.corsi;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.Corso;
import com.example.agendaCorsi.database.CorsoDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendaCorsi.ui.contatti.NuovoContatto;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoCorsi extends FunctionBase {

    TableLayout tabCorsi;
    TableRow tableRow;
    TextView descrizione, idCorso;
    Button inserisci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_elenco_corsi));
        tabCorsi = findViewById(R.id.tabellaCorsi);
        inserisci = findViewById(R.id.bInserisciCorso);

        displayElencoCorsi();

        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ElencoCorsi.this, NuovoCorso.class);
                startActivity(intent);
            }
        });
    }

    private void displayElencoCorsi() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.8);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        List<Corso> corsiList = new CorsoDAO(this).getAll();

        for (Corso corso : corsiList) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            /**
             * Caricamento descrizione corso sulla view scrollable
             */
            descrizione = new TextView(this);
            descrizione.setTextSize(16);
            descrizione.setPadding(10,20,10,20);
            descrizione.setBackground(ContextCompat.getDrawable(ElencoCorsi.this, R.drawable.cell_border));
            descrizione.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            descrizione.setGravity(Gravity.CENTER);
            descrizione.setText(String.valueOf(corso.getDescrizione()));
            descrizione.setWidth(larghezzaColonna1);
            tableRow.addView(descrizione);
            /**
             * Caricamento dell'id_corso sulla view scrollable
             */
            idCorso = new TextView(this);
            idCorso.setVisibility(View.INVISIBLE);
            idCorso.setText(String.valueOf(corso.getIdCorso()));
            idCorso.setWidth(larghezzaColonna2);
            tableRow.addView(idCorso);
            /**
             * listener su ciascuna riga
             */
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;        // in view c'è la riga selezionata
                    TextView textView = (TextView) tableRow.getChildAt(1);    // idcorso
                    Integer idCorsoSelezionato = Integer.parseInt(textView.getText().toString());
                    /**
                     * Navigazione alla funzione modifica
                     */
                    Intent intent = new Intent(ElencoCorsi.this, ModificaCorso.class);
                    intent.putExtra("idCorso", idCorsoSelezionato);
                    startActivity(intent);
                }
            });
            /**
             * Aggiungo la tableRow alla tabelle Corsi
             */
            tabCorsi.addView(tableRow);
        }
    }
}
