package com.example.agendaCorsi.ui.totali;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.access.TotaleCorsoDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.TotaleCorso;
import com.example.agendaCorsi.database.table.TotaleIscrizioniCorso;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendaCorsi.ui.contatti.ModificaContatto;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoTotali extends FunctionBase {

    TableLayout tabTotali, headerTabTotali;
    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3, larghezzaColonna4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_totali);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

        tabTotali = findViewById(R.id.tabellaTotali);
        headerTabTotali = findViewById(R.id.headerTabellaTotali);
        esci = findViewById(R.id.bExit);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);
        larghezzaColonna4 = (int) (displayMetrics.widthPixels * 0.2);

        testataElenco();
        loadTotali();

        listenerEsci(ElencoTotali.this, MainActivity.class, null);
    }

    private void loadTotali() {
        List<Object> totaliList = TotaleCorsoDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_TOT_ISCRIZIONI));
        for (Object entity : totaliList) {
            TotaleCorso totaleCorso = (TotaleCorso) entity;
            tableRow = new TableRow(this);
            tableRow.setClickable(false);
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna1, totaleCorso.getAnnoSvolgimento(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna2, totaleCorso.getDescrizioneCorso(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna3, totaleCorso.getNomeTotale(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna4, totaleCorso.getValoreTotale(), View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
            tabTotali.addView(tableRow);
        }
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Anno", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Corso", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Totale", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna4,"Valore", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        headerTabTotali.addView(tableRow);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ElencoTotali.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
