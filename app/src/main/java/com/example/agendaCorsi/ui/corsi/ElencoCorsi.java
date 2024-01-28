package com.example.agendaCorsi.ui.corsi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoCorsi extends FunctionBase {

    TableLayout tabCorsi;
    int larghezzaColonna1, larghezzaColonna2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_elenco_corsi));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem contattiItem = menu.findItem(R.id.navigation_contatti);
        contattiItem.setVisible(false);

        MenuItem corsiItem = menu.findItem(R.id.navigation_corsi);
        corsiItem.setVisible(false);

        MenuItem iscrizioniItem = menu.findItem(R.id.navigation_iscrizioni);
        iscrizioniItem.setVisible(false);

        MenuItem presenzeItem = menu.findItem(R.id.navigation_presenze);
        presenzeItem.setVisible(false);

        MenuItem exitItem = menu.findItem(R.id.navigation_esci);
        exitItem.setVisible(false);

        return true;
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
            tableRow.addView(makeCell(this,new TextView(this), corso.getStato(), 0, corso.getIdCorso(), View.TEXT_ALIGNMENT_TEXT_START, View.GONE));

            if (!corso.getStato().equals(STATO_CHIUSO)) {
                listenerTableRow(ElencoCorsi.this, ModificaCorso.class, "idCorso", null, 2);
            }
            tabCorsi.addView(tableRow);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ElencoCorsi.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
