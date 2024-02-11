package com.example.agendaCorsi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.DataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.access.CredenzialeDAO;
import com.example.agendaCorsi.database.access.DashboardDAO;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.access.GiornoSettimanaDAO;
import com.example.agendaCorsi.database.access.TotaleCorsoDAO;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Credenziale;
import com.example.agendaCorsi.database.table.Dashboard;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.GiornoSettimana;
import com.example.agendaCorsi.database.table.TotaleCorso;
import com.example.agendaCorsi.database.table.TotaleIscrizioniCorso;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.corsi.ElencoCorsi;
import com.example.agendaCorsi.ui.iscrizioni.ElencoFasceCorsi;
import com.example.agendaCorsi.ui.presenze.ElencoFasceCorsiRunning;
import com.example.agendaCorsi.ui.totali.ElencoTotali;
import com.example.agendacorsi.R;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;

import java.util.List;


public class MainActivity extends FunctionBase {

    LinearLayout dashboardMain;
    TableLayout tabSettimana;
    TextView corso, fascia, totaleGiorno;
    Context main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;
        makeToolBar(main);

        dashboardMain = findViewById(R.id.main);
        /**
         * Prelievo delle credenziali
         */
        Credenziale credenziale = new Credenziale(null, null, null, null);
        CredenzialeDAO.getInstance().select(credenziale, QueryComposer.getInstance().getQuery(QUERY_GET_CREDENZIALE));

        try {
            checkValiditaCorsi();
            checkValiditaElementiPortoflio();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        displayQuadroIscrizioni();
    }


    /**
     * Tutti gli elementi di portfolio trovati, la cui data di ultima ricarica è più vecchia di un perido temporale
     * configurato (property PORTFOLIO_ELEMENT_DURATION_YEAR), vengono posti in stato "Scaduto"
     */
    private void checkValiditaElementiPortoflio() throws Exception {
        List<Row> rows = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.TABLE_NAME, null, null, null);
        for (Row row : rows) {
            /*
             * La data ultima ricarica deve essere impostata e lo stato deve essere "Carico"
             */
            if (!coalesceValue(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMA_RICARICA))).equals("")
            && row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)).equals(STATO_CARICO)) {
                propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
                properties = propertyReader.getMyProperties("config.properties");
                int portfolioElementDurationYear = Integer.parseInt(properties.getProperty("PORTFOLIO_ELEMENT_DURATION_YEAR"));

                if (checkObsolescenceDate(addYearToDate(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMA_RICARICA)).toString(), portfolioElementDurationYear))) {
                    ConcreteDataAccessor.getInstance().update(ElementoPortfolio.TABLE_NAME, null, new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO), STATO_SCADUTO));
                }
            }
        }
    }


    /**
     * Tutti i corsi trovati outDated vengono eliminati
     */
    private void checkValiditaCorsi() throws Exception {
        List<Row> rows = ConcreteDataAccessor.getInstance().read(Corso.TABLE_NAME, null, null, null);
        for (Row row : rows) {
            if (checkObsolescenceDate(row.getColumnValue(Corso.corsoColumns.get(Corso.DATA_FINE_VALIDITA)).toString())) {
                /**
                 *  La cancellazione del corso la fa solo se i totali statistici vengono racolti
                 */
                if (raccogliTotaliCorso(row.getColumnValue(Corso.corsoColumns.get(Corso.ID_CORSO)).toString())) {
                    ConcreteDataAccessor.getInstance().delete(Corso.TABLE_NAME, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), row.getColumnValue(Corso.corsoColumns.get(Corso.ID_CORSO))));
                }
            }
        }
    }

    private boolean raccogliTotaliCorso(String idCorso) {
        /*
         *  TOTISC : totale delle iscrizioni
         */
        TotaleIscrizioniCorso totaleIscrizioniCorso = new TotaleIscrizioniCorso(idCorso, null, null, null);
        TotaleCorsoDAO.getInstance().selectTotaleIscrizioni(totaleIscrizioniCorso, QueryComposer.getInstance().getQuery(QUERY_TOT_ISCRIZIONI));

        TotaleCorso totaleCorso = new TotaleCorso(null,
                totaleIscrizioniCorso.getDescrizioneCorso(),
                totaleIscrizioniCorso.getAnnoSvolgimento(), "TOTISC", totaleIscrizioniCorso.getTotaleIscrizioni());

        if (!TotaleCorsoDAO.getInstance().insert(totaleCorso, QueryComposer.getInstance().getQuery(QUERY_INS_TOTALE_CORSO))) {
            displayAlertDialog(this, "Attenzione!", "Raccolta totale iscrizioni fallito, contatta il supporto tecnico");
            return false;
        }

        /*
         *   Altri totali qui sotto
         */



        return true;
    }

    private void displayQuadroIscrizioni() {
        TableLayout tableLayout = null;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonnaFascia = (int) (displayMetrics.widthPixels * 0.33);
        int larghezzaColonnaTotale = (int) (displayMetrics.widthPixels * 0.09);

        List<Object> totaliCorsoList = DashboardDAO.getInstance().getTotals(QueryComposer.getInstance().getQuery(QUERY_TOTALS_CORSI));

        String descrizione_corso_save = "";
        String idCorso_save = "";
        String descrizione_fascia_save = "";
        int cellNum = 1;

        LinearLayout linearLayout = findViewById(R.id.main);
        View previousView = linearLayout.findViewWithTag("dashboard-title");
        int previousId = previousView.getId();

        for (Object entity : totaliCorsoList) {
            Dashboard dashboard = Dashboard.class.cast(entity);

            if (dashboard.getDescrizioneCorso().equals(descrizione_corso_save)) {
                if (dashboard.getDescrizioneFascia().equals(descrizione_fascia_save)) {
                    // aggiungo il totale per il giorno della settimana
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana(), Integer.parseInt(dashboard.getIdFascia()), descrizione_corso_save, dashboard.getStatoCorso());
                    cellNum++;
                }
                else {
                    if (!descrizione_fascia_save.equals("")) {
                        // non è la prima row quindi aggiungo in tabella la row finita
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                        tableLayout.addView(tableRow);
                        cellNum = 1;
                    }
                    else {
                        // è la prima row quindi preparo solo la nuova riga
                        descrizione_fascia_save = dashboard.getDescrizioneFascia();
                        cellNum = 1;
                    }
                    tableRow = preparaTableRow(descrizione_fascia_save, larghezzaColonnaFascia, Integer.parseInt(dashboard.getIdFascia()), descrizione_corso_save, dashboard.getStatoCorso());
                    tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana(), Integer.parseInt(dashboard.getIdFascia()), descrizione_corso_save, dashboard.getStatoCorso());
                    cellNum = Integer.parseInt(dashboard.getGiornoSettimana()) + 1;
                }
            }
            else {
                View view = null;
                if (!descrizione_corso_save.equals("")) {
                    tableLayout.addView(tableRow);      // ultima riga preparata
                    String tableTag = "tabcorso-" + descrizione_corso_save;
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.topToBottom = previousId;
                    layoutParams.leftMargin = 20;
                    tableLayout.setLayoutParams(layoutParams);
                    tableLayout.setTag(tableTag);
                    dashboardMain.addView(tableLayout);
                    view = linearLayout.findViewWithTag(tableTag);
                    previousId = view.getId();
                    cellNum = 1;
                }

                tableLayout = new TableLayout(this);        // cambia il corso quindi devo separare su un'altra tabella

                descrizione_corso_save = dashboard.getDescrizioneCorso();
                idCorso_save = dashboard.getIdCorso();
                descrizione_fascia_save = dashboard.getDescrizioneFascia();

                previousId = titoloTabella(idCorso_save, previousId, linearLayout, dashboard.getStatoCorso());
                intestaTabella(tableLayout, larghezzaColonnaFascia, larghezzaColonnaTotale, dashboard.getStatoCorso());
                tableRow = preparaTableRow(descrizione_fascia_save, larghezzaColonnaFascia, Integer.parseInt(dashboard.getIdFascia()), descrizione_corso_save, dashboard.getStatoCorso());
                tableRow = aggiungiTotaleGiorno(tableRow, dashboard.getTotaleFascia(), larghezzaColonnaTotale, cellNum, dashboard.getGiornoSettimana(), Integer.parseInt(dashboard.getIdFascia()), descrizione_corso_save, dashboard.getStatoCorso());
                cellNum = Integer.parseInt(dashboard.getGiornoSettimana()) + 1;
            }
        }
        if (totaliCorsoList.size() > 0) {
            tableLayout.addView(tableRow);
        }
        String tableTag = "tabcorso-" + descrizione_corso_save;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topToBottom = previousId;
        layoutParams.leftMargin = 20;
        tableLayout.setLayoutParams(layoutParams);
        tableLayout.setTag(tableTag);
        dashboardMain.addView(tableLayout);
        setContentView(dashboardMain);
    }

    public int titoloTabella(String idCorso, int tagToConstraint, LinearLayout linearLayout, String statoCorso) {
        Corso _corso = new Corso(idCorso, null, null, null, null, null, null, null, null);
        CorsoDAO.getInstance().select(_corso, QueryComposer.getInstance().getQuery(QUERY_GET_CORSO));
        String dataDa = dateFormat(_corso.getDataInizioValidita().replace("#", ""), "yyyy-MM-dd", "dd-MM-yyyy");
        String dataA  = dateFormat(_corso.getDataFineValidita().replace("#", ""), "yyyy-MM-dd", "dd-MM-yyyy");

        String textViewTag = "corso-" + _corso.getDescrizione();

        TextView corso = new TextView(this);
        corso.setText(_corso.getDescrizione() + " : " + dataDa + " - " + dataA);
        corso.setTextColor(getResources().getColor(R.color.black, getTheme()));
        corso.setTextSize(16);
        corso.setPadding(10,50,10,50);
        corso.setTypeface(Typeface.DEFAULT_BOLD);
        corso.setId(Integer.parseInt(idCorso));
        corso.setTag(textViewTag);

        if (!statoCorso.equals(STATO_SOSPESO)) {
            listenerOnCorso(corso, Integer.parseInt(idCorso), this);
        }

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topToBottom = tagToConstraint;
        layoutParams.leftMargin = 20;
        corso.setLayoutParams(layoutParams);
        dashboardMain.addView(corso);

        View view = linearLayout.findViewWithTag(textViewTag);
        return view.getId();
    }

    public void intestaTabella(TableLayout tableLayout, int larghezzaColonnaFascia, int larghezzaColonnaTotale, String statoCorso) {

        tableRow = new TableRow(this);
        tableRow.setClickable(false);

        String headerType = (statoCorso.equals(STATO_ATTIVO)) ? HEADER : HEADER_OFF;

        tableRow.addView(makeCell(this,new TextView(this), headerType, larghezzaColonnaFascia,"Fascia", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

        List<Object> giorniSettimanaList = GiornoSettimanaDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_GIORNI_SETTIMANA));

        for (Object entity : giorniSettimanaList) {
            GiornoSettimana giornoSettimana = (GiornoSettimana) entity;
            GiornoSettimana gioSet = new GiornoSettimana(String.valueOf(getDayOfWeek()), null, null);
            GiornoSettimanaDAO.getInstance().select(gioSet, QueryComposer.getInstance().getQuery(QUERY_GET_GIORNO_SETTIMANA));
            if (statoCorso.equals(STATO_ATTIVO)) {
                headerType = (gioSet.getNomeGiornoAbbreviato().equals(giornoSettimana.getNomeGiornoAbbreviato())) ? HEADER_EVIDENCE : HEADER;
            } else {
                headerType = HEADER_OFF;
            }
            tableRow.addView(makeCell(this,new TextView(this), headerType, larghezzaColonnaTotale,giornoSettimana.getNomeGiornoAbbreviato(), View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        }
        tableLayout.addView(tableRow);
    }


    public TableRow preparaTableRow(String descrizioneFascia, int larghezzaColonna, int idFascia, String corso, String statoCorso) {
        tableRow = new TableRow(this);
        String detailType = (isFasciaRunning(descrizioneFascia)) ? DETAIL_EVIDENCE : DETAIL_SIMPLE;
        if (!statoCorso.equals(STATO_ATTIVO)) {
            detailType = DETAIL_SIMPLE;
            tableRow.addView(makeCell(this,new TextView(this), detailType, larghezzaColonna, descrizioneFascia, View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        } else {
            tableRow.addView(listenerOnFascia(makeCell(this,new TextView(this), detailType, larghezzaColonna, descrizioneFascia, View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE),  idFascia, corso, main));
        }
        return tableRow;
    }

    public TableRow aggiungiTotaleGiorno(TableRow tRow, String totale, int larghezzaColonna, int cellNum, String giornoSettimana, int idFascia, String corso, String statoCorso) {
        fillRow(Integer.parseInt(giornoSettimana), cellNum, larghezzaColonna, tRow);
        totaleGiorno = new TextView(this);
        if (statoCorso.equals(STATO_ATTIVO)) {
            if (Integer.parseInt(totale) > 0) {
                tRow.addView(listenerOnTotale(makeCell(main, new TextView(this), DETAIL_SIMPLE, larghezzaColonna, totale, View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE), idFascia, corso, totale, main));
            } else {
                tRow.addView(makeCell(main, new TextView(this), DETAIL_SIMPLE, larghezzaColonna, totale, View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
            }
        } else {
            tRow.addView(makeCell(main, new TextView(this), DETAIL_SIMPLE, larghezzaColonna, totale, View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        }
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

        if (!TotaleCorsoDAO.getInstance().existRows(QueryComposer.getInstance().getQuery(QUERY_GETALL_TOT_ISCRIZIONI))) {
            MenuItem totaliItem = menu.findItem(R.id.navigation_totali);
            totaliItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Contatti")) {
            Intent intent = new Intent(MainActivity.this, ElencoContatti.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getTitle().equals("Corsi")) {
            Intent intent = new Intent(MainActivity.this, ElencoCorsi.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getTitle().equals("Iscrizioni")) {
            Intent intent = new Intent(MainActivity.this, ElencoFasceCorsi.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        else if (item.getTitle().equals("Presenze")) {
            Intent intent = new Intent(MainActivity.this, ElencoFasceCorsiRunning.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        else if (item.getTitle().equals("Totali")) {
            Intent intent = new Intent(MainActivity.this, ElencoTotali.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        else if (item.getTitle().equals("Esci")) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}