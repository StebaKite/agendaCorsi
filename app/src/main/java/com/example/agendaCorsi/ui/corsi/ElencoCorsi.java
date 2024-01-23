package com.example.agendaCorsi.ui.corsi;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoCorsi extends FunctionBase {

    TableLayout tabCorsi;
    int larghezzaColonna1, larghezzaColonna2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_elenco_corsi));
        tabCorsi = findViewById(R.id.tabellaCorsi);
        inserisci = findViewById(R.id.bInserisciCorso);
        esci = findViewById(R.id.bExit);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.8);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        testataElenco();
        loadCorsi();

        listenerEsci(ElencoCorsi.this, MainActivity.class, null);
        listenerInserisci(ElencoCorsi.this, NuovoCorso.class, null);
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome corso", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tabCorsi.addView(tableRow);
    }

    private void loadCorsi() {

        List<Object> corsiList = CorsoDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_CORSI));

        for (Object entity : corsiList) {
            Corso corso = Corso.class.cast(entity);

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this,new TextView(this), corso.getStato(), larghezzaColonna1, corso.getDescrizione(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), corso.getStato(), larghezzaColonna2, corso.getStato(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), corso.getStato(), 0, corso.getIdCorso(), View.TEXT_ALIGNMENT_TEXT_START, View.INVISIBLE));

            if (!corso.getStato().equals(STATO_CHIUSO)) {
                listenerTableRow(ElencoCorsi.this, ModificaCorso.class, "idCorso", null, 2);
            }
            tabCorsi.addView(tableRow);
        }
    }
}
