package com.example.agendaCorsi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.access.CredenzialeDAO;
import com.example.agendaCorsi.database.access.DashboardDAO;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.access.GiornoSettimanaDAO;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Credenziale;
import com.example.agendaCorsi.database.table.Dashboard;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.GiornoSettimana;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.corsi.ElencoCorsi;
import com.example.agendaCorsi.ui.iscrizioni.ElencoFasceCorsi;
import com.example.agendaCorsi.ui.presenze.ElencoFasceCorsiRunning;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

        tabSettimana = findViewById(R.id.tabellaSettimana);
        /**
         * Prelievo delle credenziali
         */
        Credenziale credenziale = new Credenziale(null, null, null, null);
        CredenzialeDAO.getInstance().select(credenziale, QueryComposer.getInstance().getQuery(QUERY_GET_CREDENZIALE));

        checkValiditaCorsi();
        checkValiditaElementiPortoflio();
        displayQuadroIscrizioni();
    }

    /**
     * Tutti gli elementi di portfolio trovati, la cui data di ultima ricarica è più vecchia di un perido temporale
     * configurato (property PORTFOLIO_ELEMENT_DURATION_YEAR), vengono posti in stato "Scaduto"
     */
    private void checkValiditaElementiPortoflio() {
        List<Object> elementiList = ElementoPortfolioDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_ELEMENTO));
        for (Object entity : elementiList) {
            ElementoPortfolio elementoPortfolio = (ElementoPortfolio) entity;
            /*
             * La data ultima ricarica deve essere impostata e lo stato deve essere "Carico"
             */
            if (!elementoPortfolio.getDataUltimaRicarica().equals("") && elementoPortfolio.getStato().equals(STATO_CARICO)) {
                propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
                properties = propertyReader.getMyProperties("config.properties");
                int portfolioElementDurationYear = Integer.parseInt(properties.getProperty("PORTFOLIO_ELEMENT_DURATION_YEAR"));

                if (checkObsolescenceDate(addYearToDate(elementoPortfolio.getDataUltimaRicarica(), portfolioElementDurationYear))) {
                    elementoPortfolio.setStato(STATO_SCADUTO);
                    if (!ElementoPortfolioDAO.getInstance().updateStato(elementoPortfolio, QueryComposer.getInstance().getQuery(QUERY_DEL_ELEMENTO))) {
                        displayAlertDialog(this, "Attenzione!", "Aggiornamento stato elemento_portfolio fallito, contatta il supporto tecnico");
                    }
                }
            }
        }
    }

    /**
     * Tutti i corsi trovati outDated vengono eliminati
     */
    private void checkValiditaCorsi() {
        List<Object> corsiList = CorsoDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_CORSI));
        for (Object entity : corsiList) {
            Corso corso = Corso.class.cast(entity);
            if (checkObsolescenceDate(corso.getDataFineValidita())) {
                if (!CorsoDAO.getInstance().delete(corso, QueryComposer.getInstance().getQuery(QUERY_DEL_CORSO))) {
                    displayAlertDialog(this, "Attenzione!", "Cancellazione corso fallito, contatta il supporto tecnico");
                }
            }
        }
    }

    private void displayQuadroIscrizioni() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonnaCorso = (int) (displayMetrics.widthPixels * 0.25);
        int larghezzaColonnaFascia = (int) (displayMetrics.widthPixels * 0.25);
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
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana());
                    cellNum++;
                }
                else {
                    if (!descrizione_fascia_save.equals("")) {
                        // non è la prima row quindi aggiungo in tabella la row finita
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                        tabSettimana.addView(tableRow);
                        cellNum = 1;
                    }
                    else {
                        // è la prima row quindi preparo solo la nuova riga
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                        cellNum = 1;
                    }
                    tableRow = preparaTableRow(descrizione_fascia_save, larghezzaColonnaFascia);
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana());
                    cellNum = Integer.parseInt(dashboard.getGiornoSettimana()) + 1;
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
                cellNum = Integer.parseInt(dashboard.getGiornoSettimana()) + 1;
            }
        }
        if (totaliCorsoList.size() > 0) {
            tabSettimana.addView(tableRow);
        }
    }

    public void intestaTabella(String descrizioneCorso, int larghezzaColonna, int larghezzaColonnaFascia, int larghezzaColonnaTotale) {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        corso = new TextView(this);
        corso.setTextSize(16);
        corso.setPadding(10,50,10,50);
        corso.setWidth(larghezzaColonna);
        corso.setText(descrizioneCorso);
        corso.setTypeface(Typeface.DEFAULT_BOLD);
        tableRow.addView(corso);
        tabSettimana.addView(tableRow);

        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonnaFascia,"Fascia", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

        List<Object> giorniSettimanaList = GiornoSettimanaDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_GIORNI_SETTIMANA));

        for (Object entity : giorniSettimanaList) {
            GiornoSettimana giornoSettimana = (GiornoSettimana) entity;
            GiornoSettimana gioSet = new GiornoSettimana(String.valueOf(getDayOfWeek()), null, null);
            GiornoSettimanaDAO.getInstance().select(gioSet, QueryComposer.getInstance().getQuery(QUERY_GET_GIORNO_SETTIMANA));
            String headerType = (gioSet.getNomeGiornoAbbreviato().equals(giornoSettimana.getNomeGiornoAbbreviato())) ? HEADER_EVIDENCE : HEADER;

            tableRow.addView(makeCell(this,new TextView(this), headerType, larghezzaColonnaTotale,giornoSettimana.getNomeGiornoAbbreviato(), View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        }
        tabSettimana.addView(tableRow);
    }


    public TableRow preparaTableRow(String descrizioneFascia, int larghezzaColonna) {
        tableRow = new TableRow(this);
        String detailType = (isFasciaRunning(descrizioneFascia)) ? DETAIL_EVIDENCE : DETAIL_SIMPLE;
        tableRow.addView(makeCell(this,new TextView(this), detailType, larghezzaColonna, descrizioneFascia, View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        return tableRow;
    }

    public TableRow aggiungiTotaleGiorno(TableRow tRow, String totale, int larghezzaColonna, int cellNum, String giornoSettimana) {
        fillRow(Integer.parseInt(giornoSettimana), cellNum, larghezzaColonna, tRow);
        totaleGiorno = new TextView(this);
        tRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna, totale, View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        return tRow;
    }

    private TableRow fillRow(int giornoSettimana, int cellNum, int larghezzaColonna, TableRow tRow) {
        for (int i = cellNum; i < giornoSettimana; i++) {
            tRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna, "", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        }
        return tRow;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem homeItem = menu.findItem(R.id.navigation_home);
        homeItem.setVisible(false);

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
        else if (item.getTitle().equals("Presenze")) {
            Intent intent = new Intent(MainActivity.this, ElencoFasceCorsiRunning.class);
            startActivity(intent);
            return true;
        }
        else if (item.getTitle().equals("Esci")) {
            MainActivity.this.finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
}