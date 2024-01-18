package com.example.agendaCorsi;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.agendaCorsi.database.access.CredenzialeDAO;
import com.example.agendaCorsi.database.access.DashboardDAO;
import com.example.agendaCorsi.database.access.GiornoSettimanaDAO;
import com.example.agendaCorsi.database.table.Credenziale;
import com.example.agendaCorsi.database.table.Dashboard;
import com.example.agendaCorsi.database.table.GiornoSettimana;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.corsi.ElencoCorsi;
import com.example.agendaCorsi.ui.iscrizioni.ElencoFasceCorsi;
import com.example.agendaCorsi.ui.settaggi.ModificaSettaggi;
import com.example.agendacorsi.R;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;

import java.util.List;


public class MainActivity extends FunctionBase {

    TableLayout tabSettimana;
    TextView corso, fascia, totaleGiorno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabSettimana = findViewById(R.id.tabellaSettimana);
        /**
         * Prelievo delle credenziali
         */
        Credenziale credenziale = new Credenziale(null, null, null, null);
        CredenzialeDAO.getInstance().select(credenziale, QueryComposer.getInstance().getQuery(QUERY_GET_CREDENZIALE));

        displayQuadroIscrizioni();
    }

    private void displayQuadroIscrizioni() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonnaCorso = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonnaFascia = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonnaTotale = (int) (displayMetrics.widthPixels * 0.1);

        List<Object> totaliCorsoList = DashboardDAO.getInstance().getTotals(QueryComposer.getInstance().getQuery(QUERY_TOTALS_CORSI));

        String descrizione_corso_save = "";
        String descrizione_fascia_save = "";
        int cellNum = 1;

        for (Object entity : totaliCorsoList) {
            Dashboard dashboard = Dashboard.class.cast(entity);

            if (dashboard.getDescrizioneCorso().equals(descrizione_corso_save)) {
                if (dashboard.getDescrizioneFascia().equals(descrizione_fascia_save)) {
                    // aggiungo il totale per il giorno della settimana
                    cellNum++;
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana());
                    cellNum = Integer.parseInt(dashboard.getGiornoSettimana());
                }
                else {
                    cellNum++;
                    if (!descrizione_fascia_save.equals("")) {
                        // non è la prima row quindi aggiungo in tabella la row finita
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                        tabSettimana.addView(tableRow);
                        cellNum = 0;
                    }
                    else {
                        // è la prima row quindi preparo solo la nuova riga
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                        cellNum = 0;
                    }
                    tableRow = preparaTableRow(descrizione_fascia_save, larghezzaColonnaFascia);
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana());
                    cellNum = Integer.parseInt(dashboard.getGiornoSettimana());
                }
            }
            else {
                // cambia il corso quindi devo separare su un'altra tabella
                if (!descrizione_corso_save.equals("")) {
                    tabSettimana.addView(tableRow);
                    cellNum = 1;
                }
                descrizione_corso_save = dashboard.getDescrizioneCorso();
                descrizione_fascia_save = dashboard.getDescrizioneFascia();
                intestaTabella(descrizione_corso_save, larghezzaColonnaCorso, larghezzaColonnaFascia, larghezzaColonnaTotale);
                tableRow = preparaTableRow(descrizione_fascia_save, larghezzaColonnaFascia);
                tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana());
                cellNum = 1;
            }
        }
        if (totaliCorsoList.size() > 0) {
            tabSettimana.addView(tableRow);
        }
    }

    public void intestaTabella(String descrizioneCorso, int larghezzaColonna, int larghezzaColonnaFascia, int larghezzaColonnaTotale) {
        tableRow = new TableRow(this);
        corso = new TextView(this);
        corso.setTextSize(20);
        corso.setPadding(10,50,10,50);
        corso.setWidth(larghezzaColonna);
        corso.setText(descrizioneCorso);
        tableRow.addView(corso);
        tabSettimana.addView(tableRow);

        tableRow = new TableRow(this);

        fascia = new TextView(this);
        fascia.setTextSize(14);
        fascia.setPadding(10,20,10,20);
        fascia.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_border));
        fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        fascia.setGravity(Gravity.CENTER);
        fascia.setWidth(larghezzaColonnaFascia);
        fascia.setText("Fascia");
        tableRow.addView(fascia);

        List<Object> giorniSettimanaList = GiornoSettimanaDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_GIORNI_SETTIMANA));

        for (Object entity : giorniSettimanaList) {
            GiornoSettimana giornoSettimana = (GiornoSettimana) entity;

            totaleGiorno = new TextView(this);
            totaleGiorno.setTextSize(14);
            totaleGiorno.setPadding(10,20,10,20);
            totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
            totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            totaleGiorno.setGravity(Gravity.CENTER);
            totaleGiorno.setWidth(larghezzaColonnaTotale);
            totaleGiorno.setText(giornoSettimana.getNomeGiornoAbbreviato());
            tableRow.addView(totaleGiorno);
        }
        tabSettimana.addView(tableRow);
    }


    public TableRow preparaTableRow(String descrizioneFascia, int larghezzaColonna) {
        tableRow = new TableRow(this);
        fascia = new TextView(this);
        fascia.setTextSize(14);
        fascia.setPadding(10,20,10,20);
        fascia.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        fascia.setGravity(Gravity.CENTER);
        fascia.setText(descrizioneFascia);
        fascia.setWidth(larghezzaColonna);
        tableRow.addView(fascia);
        return tableRow;
    }

    public TableRow aggiungiTotaleGiorno(TableRow tRow, String totale, int larghezzaColonna, int cellNum, String giornoSettimana) {
        //fillRow(Integer.parseInt(giornoSettimana), cellNum, larghezzaColonna, tRow);
        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(14);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_border));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setText(totale);
        totaleGiorno.setWidth(larghezzaColonna);

        tRow.addView(totaleGiorno);

        return tRow;
    }

    private TableRow fillRow(int giornoSettimana, int cellNum, int larghezzaColonna, TableRow tRow) {
        for (int i = cellNum; i < giornoSettimana; i++) {
            totaleGiorno = new TextView(this);
            totaleGiorno.setTextSize(16);
            totaleGiorno.setPadding(10, 20, 10, 20);
            totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_border));
            totaleGiorno.setWidth(larghezzaColonna);
            tRow.addView(totaleGiorno);
        }
        return tRow;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Contatti")) {
            Intent intent = new Intent(MainActivity.this, ElencoContatti.class);
            startActivity(intent);
            return true;
        } else if (item.getTitle().equals("Corsi")) {
            Intent intent = new Intent(MainActivity.this, ElencoCorsi.class);
            startActivity(intent);
            return true;
        } else if (item.getTitle().equals("Iscrizioni")) {
            Intent intent = new Intent(MainActivity.this, ElencoFasceCorsi.class);
            startActivity(intent);
            return true;
        }
        else if (item.getTitle().equals("Settaggi")) {
            Intent intent = new Intent(MainActivity.this, ModificaSettaggi.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}