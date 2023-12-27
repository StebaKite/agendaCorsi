package com.example.agendaCorsi.ui.contatti;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agendacorsi.R;
import com.example.agendaCorsi.database.ContattiDAO;
import com.example.agendaCorsi.database.Contatto;
import java.util.List;

public class ElencoContatti extends AppCompatActivity {

    TableLayout tabContatti;
    TableRow tableRow;
    TextView nomeContatto, idContatto;
    Button inserisci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_contatti);
        tabContatti = findViewById(R.id.tabellaContatti);
        inserisci = findViewById(R.id.bInserisciContatto);

        displayElencoContatti();
        /**
         * implemento il listener per il bottone di inserimento nuovo contatto
         */
        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ElencoContatti.this, NuovoContatto.class);
                startActivity(intent);
            }
        });
    }

    private void displayElencoContatti() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.8);

        List<Contatto> contattiList = new ContattiDAO(this).getAll();

        int riga = 0;
        for (Contatto contatto : contattiList) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            /**
             * Caricamento ID contatto sul textView
             */
            idContatto = new TextView(this);
            idContatto.setTextSize(18);
            idContatto.setPadding(0,10,0,10);
            idContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border));
            idContatto.setGravity(Gravity.CENTER);
            idContatto.setText(String.valueOf(contatto.getId()));
            idContatto.setWidth(larghezzaColonna1);
            if (riga % 2 == 1) idContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.color.alt_background_row));
            tableRow.addView(idContatto);
            /**
             * Caricamento del nome sul textView
             */
            nomeContatto = new TextView(this);
            nomeContatto.setTextSize(18);
            nomeContatto.setPadding(0,10,0,10);
            nomeContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border));
            nomeContatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nomeContatto.setGravity(Gravity.CENTER);
            nomeContatto.setText(String.valueOf(contatto.getNome()));
            nomeContatto.setWidth(larghezzaColonna2);
            if (riga % 2 == 1) nomeContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.color.alt_background_row));
            tableRow.addView(nomeContatto);
            /**
             * implemento il listener sul click della riga per vedere la scheda del contatto
             */
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;        // in view c'è la riga selezionata
                    TextView textView = (TextView) tableRow.getChildAt(0);    // idcontatto
                    Integer idContattoSelezionato = Integer.parseInt(textView.getText().toString());
                    /**
                     * Passo all'attività ModificaContatto l'id del contatto selezionato
                     */
                    Intent intent = new Intent(ElencoContatti.this, ModificaContatto.class);
                    intent.putExtra("id", idContattoSelezionato);
                    startActivity(intent);
                }
            });
            /**
             * aggiungo la riga alla tabella dei contatti
             */
            tabContatti.addView(tableRow);
            riga++;
        }
    }
}
