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

import com.example.agendaCorsi.database.access.DashboardDAO;
import com.example.agendaCorsi.database.table.Dashboard;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.corsi.ElencoCorsi;
import com.example.agendaCorsi.ui.iscrizioni.ElencoFasceCorsi;
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

        displayQuadroIscrizioni();
    }

    private void displayQuadroIscrizioni() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonnaCorso = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonnaFascia = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonnaTotale = (int) (displayMetrics.widthPixels * 0.1);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        List<Object> totaliCorsoList = new DashboardDAO(this).getTotals(properties.getProperty(QUERY_TOTALS_CORSI));

        String descrizione_corso_save = "";
        String descrizione_fascia_save = "";
        int cellaNum = 1;

        for (Object entity : totaliCorsoList) {
            Dashboard dashboard = Dashboard.class.cast(entity);

            if (dashboard.getDescrizioneCorso().equals(descrizione_corso_save)) {
                if (dashboard.getDescrizioneFascia().equals(descrizione_fascia_save)) {
                    // aggiungo il totale per il giorno della settimana
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, dashboard.getGiornoSettimana(), cellaNum);
                    cellaNum = Integer.parseInt(dashboard.getGiornoSettimana()) + 1;
                }
                else {
                    if (!descrizione_fascia_save.equals("")) {
                        // non è la prima row quindi aggiungo in tabella la row finita
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                        fillTableCell(tableRow, larghezzaColonnaTotale, cellaNum);
                        tabSettimana.addView(tableRow);
                        cellaNum = 1;
                    }
                    else {
                        // è la prima row quindi preparo solo la nuova riga
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                    }
                    tableRow = preparaTableRow(descrizione_fascia_save, larghezzaColonnaFascia);
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, dashboard.getGiornoSettimana(), cellaNum);
                    cellaNum = Integer.parseInt(dashboard.getGiornoSettimana()) + 1;
                }
            }
            else {
                // cambia il corso quindi devo separare su un'altra tabella
                descrizione_corso_save = dashboard.getDescrizioneCorso();
                descrizione_fascia_save = dashboard.getDescrizioneFascia();
                intestaTabella(descrizione_corso_save, larghezzaColonnaCorso, larghezzaColonnaFascia, larghezzaColonnaTotale);
                tableRow = preparaTableRow(descrizione_fascia_save, larghezzaColonnaFascia);
                tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, dashboard.getGiornoSettimana(), cellaNum);
                cellaNum = Integer.parseInt(dashboard.getGiornoSettimana()) + 1;
            }
        }
        // riempio tutte le celle rimaste vuote e aggiungo in tabella l'ultima riga composta
        fillTableCell(tableRow, larghezzaColonnaTotale, cellaNum);
        tabSettimana.addView(tableRow);
    }

    private TableRow fillTableCell(TableRow tRow, int larghezzaColonna, int cellaNum) {
        if (cellaNum <= 7) {
            for (int i = cellaNum; i < 8; i++) {
                totaleGiorno = new TextView(this);
                totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_border));
                totaleGiorno.setWidth(larghezzaColonna);
                tRow.addView(totaleGiorno);
            }
        }
        return tRow;
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
        fascia.setTextSize(16);
        fascia.setPadding(10,20,10,20);
        fascia.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_border));
        fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        fascia.setGravity(Gravity.CENTER);
        fascia.setWidth(larghezzaColonnaFascia);
        fascia.setText("Fascia");
        tableRow.addView(fascia);

        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setWidth(larghezzaColonnaTotale);
        totaleGiorno.setText("Lu");
        tableRow.addView(totaleGiorno);

        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setWidth(larghezzaColonnaTotale);
        totaleGiorno.setText("Ma");
        tableRow.addView(totaleGiorno);

        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setWidth(larghezzaColonnaTotale);
        totaleGiorno.setText("Me");
        tableRow.addView(totaleGiorno);

        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setWidth(larghezzaColonnaTotale);
        totaleGiorno.setText("Gi");
        tableRow.addView(totaleGiorno);

        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setWidth(larghezzaColonnaTotale);
        totaleGiorno.setText("Ve");
        tableRow.addView(totaleGiorno);

        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setWidth(larghezzaColonnaTotale);
        totaleGiorno.setText("Sa");
        tableRow.addView(totaleGiorno);

        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setWidth(larghezzaColonnaTotale);
        totaleGiorno.setText("Do");
        tableRow.addView(totaleGiorno);

        tabSettimana.addView(tableRow);
    }


    public TableRow preparaTableRow(String descrizioneFascia, int larghezzaColonna) {
        tableRow = new TableRow(this);
        fascia = new TextView(this);
        fascia.setTextSize(16);
        fascia.setPadding(10,20,10,20);
        fascia.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_heading));
        fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        fascia.setGravity(Gravity.CENTER);
        fascia.setText(descrizioneFascia);
        fascia.setWidth(larghezzaColonna);
        tableRow.addView(fascia);
        return tableRow;
    }

    public TableRow aggiungiTotaleGiorno(TableRow tRow, String totale, int larghezzaColonna, String giornoNum, int cellaNum) {
        /*
         * Se serve aggiungo delle celle vuote sino a coincidere il giorno estratto con il numero della cella relativo
         * Le celle visualizzate devono essere sempre 7 pari ai giorni della settimana
         */
        int giorno = Integer.parseInt(giornoNum);
        if (giorno > cellaNum) {
            for (int i = cellaNum; i < giorno; i++) {
                totaleGiorno = new TextView(this);
                totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_border));
                totaleGiorno.setWidth(larghezzaColonna);
                tRow.addView(totaleGiorno);
            }
        }
        totaleGiorno = new TextView(this);
        totaleGiorno.setTextSize(16);
        totaleGiorno.setPadding(10,20,10,20);
        totaleGiorno.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_border));
        totaleGiorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleGiorno.setGravity(Gravity.CENTER);
        totaleGiorno.setText(totale);
        totaleGiorno.setWidth(larghezzaColonna);


        tRow.addView(totaleGiorno);

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
        return super.onOptionsItemSelected(item);
    }
}