package com.example.agendaCorsi.ui.corsi;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoCorsi extends FunctionBase {

    TableLayout tabCorsi;
    TextView descrizione, stato, idCorso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_elenco_corsi));
        tabCorsi = findViewById(R.id.tabellaCorsi);
        inserisci = findViewById(R.id.bInserisciCorso);

        displayElencoCorsi();
        listenerEsci(ElencoCorsi.this, MainActivity.class, null);
        listenerInserisci(ElencoCorsi.this, NuovoCorso.class, null);
    }

    private void displayElencoCorsi() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.8);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        List<Object> corsiList = new CorsoDAO(this).getAll(properties.getProperty(QUERY_GETALL_CORSI));

        for (Object entity : corsiList) {
            Corso corso = Corso.class.cast(entity);

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

            stato = new TextView(this);
            stato.setTextSize(16);
            stato.setPadding(10,20,10,20);
            stato.setBackground(ContextCompat.getDrawable(ElencoCorsi.this, R.drawable.cell_border));
            stato.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            stato.setGravity(Gravity.CENTER);
            stato.setText(String.valueOf(corso.getStato()));
            stato.setWidth(larghezzaColonna2);
            tableRow.addView(stato);

            idCorso = new TextView(this);
            idCorso.setVisibility(View.INVISIBLE);
            idCorso.setText(String.valueOf(corso.getIdCorso()));
            tableRow.addView(idCorso);

            listenerTableRow(ElencoCorsi.this, ModificaCorso.class, "idCorso", null, 2);
            tabCorsi.addView(tableRow);
        }
    }
}
