package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.database.table.TotaleCorso;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ModificaCorso extends FunctionBase {

    String idCorso, oraInizioFascia, oraFineFascia;
    String descrizioneCorso, sport, dataInizioValidita, dataFineValidita;
    EditText _descrizione, _sport, _dataInizioValidita, _dataFineValidita;
    Context modificaCorso;

    final Calendar myCalendar = Calendar.getInstance();
    public TableLayout tabellaFasceModCorso, headerTabellaFasceCorso;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_corso);

        makeToolBar(this);

        headerTabellaFasceCorso = findViewById(R.id.headerTabellaContattiIscrivibili);
        tabellaFasceModCorso = findViewById(R.id.tabellaFasceModCorso);

        Intent intent = getIntent();
        idCorso = intent.getStringExtra("idCorso");
        oraInizioFascia = intent.getStringExtra("oraInizioFascia");
        oraFineFascia = intent.getStringExtra("oraFineFascia");

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        elimina = findViewById(R.id.bElimina);
        salva = findViewById(R.id.bSalva);
        chiudi = findViewById(R.id.bChiudi);
        sospendi = findViewById(R.id.bSospendi);
        apri = findViewById(R.id.bApri);
        inserisci = findViewById(R.id.NuovaFasciaButton);

        _descrizione = findViewById(R.id.editDescrizione);

        /**
         * Date picker data inizio validità
         */
        _dataInizioValidita = findViewById(R.id.editDataInizio);

        DatePickerDialog.OnDateSetListener dataInizio = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(_dataInizioValidita, myCalendar);
            }
        };
        _dataInizioValidita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModificaCorso.this, dataInizio, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**
         * Date picker data fine validità
         */
        _dataFineValidita = findViewById(R.id.editDataFine);

        DatePickerDialog.OnDateSetListener dataFine = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(_dataFineValidita, myCalendar);
            }
        };
        _dataFineValidita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ModificaCorso.this, dataFine, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        modificaCorso = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.3);

        String statoCorso = loadCorso();
        testataElenco();
        loadFasceOrarie();

        /*
         * La visibilità dei bottoni di cambio stato rispetta le regole
         * documentate dal diagramma degli stati del Corso
         */
        if (statoCorso.equals(STATO_CHIUSO)) {
            chiudi.setVisibility(View.GONE);
            sospendi.setVisibility(View.GONE);
            apri.setVisibility(View.GONE);
        } else if (statoCorso.equals(STATO_APERTO)) {
            apri.setVisibility(View.GONE);
            listenerChiudi();
            listenerSospendi();
        } else if (statoCorso.equals(STATO_SOSPESO)) {
            chiudi.setVisibility(View.GONE);
            sospendi.setVisibility(View.GONE);
            listenerApri();
        } else if (statoCorso.equals(STATO_ATTIVO)) {
            apri.setVisibility(View.GONE);
            listenerChiudi();
            listenerSospendi();
        }

        /*
         * Set dei listeners sui bottoni
         */
        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idCorso", String.valueOf(idCorso));
        intentMap.put("descrizioneCorso", descrizioneCorso);

        listenerEsci(modificaCorso, ElencoCorsi.class, intentMap);
        listenerAnnulla();
        listenerElimina();
        listenerSalva();
        listenerInserisci(modificaCorso, NuovaFascia.class, intentMap);
    }


    @Override
    public void makeAnnulla() {
        _descrizione.setText(descrizioneCorso);
        _dataInizioValidita.setText(dataInizioValidita);
        _dataFineValidita.setText(dataFineValidita);
        makeToastMessage(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito").show();
    }

    @Override
    public void makeApri() {
        try {
            List<Row> rows = ConcreteDataAccessor.getInstance().read(TotaleCorso.VIEW_TOT_ISCRIZIONI, null, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso), null);

            String statoCorso = STATO_APERTO;
            for (Row row : rows) {
                if (row.getColumnValue(TotaleCorso.totIscrizioniCorsoColumns.get(TotaleCorso.DESCRIZIONE_CORSO)) != null) {
                    if (Integer.parseInt(row.getColumnValue(TotaleCorso.totIscrizioniCorsoColumns.get(TotaleCorso.VALORE_TOTALE)).toString()) > 0) {
                        statoCorso = STATO_ATTIVO;
                    }
                }
                Row rowToUpdate = new Row();
                rowToUpdate.addColumn(Corso.corsoColumns.get(Corso.STATO), statoCorso);
                rowToUpdate.addColumn(Corso.corsoColumns.get(Corso.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());

                ConcreteDataAccessor.getInstance().update(Corso.TABLE_NAME, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso), rowToUpdate);

                makeToastMessage(AgendaCorsiApp.getContext(), "Corso aperto con successo").show();
                esci.callOnClick();
            }
        }
        catch (Exception e) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeSospendi() {
        try {
            Row rowToUpdate = new Row();
            rowToUpdate.addColumn(Corso.corsoColumns.get(Corso.STATO), STATO_SOSPESO);
            rowToUpdate.addColumn(Corso.corsoColumns.get(Corso.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());

            ConcreteDataAccessor.getInstance().update(Corso.TABLE_NAME, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso), rowToUpdate);
            makeToastMessage(AgendaCorsiApp.getContext(), "Corso sospeso con successo").show();
            esci.callOnClick();
        }
        catch (Exception e) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeChiudi() {
        try {
            Row rowToUpdate = new Row();
            rowToUpdate.addColumn(Corso.corsoColumns.get(Corso.STATO), STATO_CHIUSO);
            rowToUpdate.addColumn(Corso.corsoColumns.get(Corso.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());

            ConcreteDataAccessor.getInstance().update(Corso.TABLE_NAME, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso), rowToUpdate);
            makeToastMessage(AgendaCorsiApp.getContext(), "Corso chiuso con successo").show();
            esci.callOnClick();
        }
        catch (Exception e) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeSalva() {
        if (_descrizione.getText().toString().equals("") ||
            _dataInizioValidita.getText().toString().equals("") ||
            _dataFineValidita.getText().toString().equals("")) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            try {
                Row updateRow = new Row();
                updateRow.addColumn(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso);
                updateRow.addColumn(Corso.corsoColumns.get(Corso.DESCRIZIONE), _descrizione.getText().toString());
                updateRow.addColumn(Corso.corsoColumns.get(Corso.DATA_INIZIO_VALIDITA), dateFormat(_dataInizioValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
                updateRow.addColumn(Corso.corsoColumns.get(Corso.DATA_FINE_VALIDITA), dateFormat(_dataFineValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
                updateRow.addColumn(Corso.corsoColumns.get(Corso.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());

                ConcreteDataAccessor.getInstance().update(Corso.TABLE_NAME, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso), updateRow);
                makeToastMessage(AgendaCorsiApp.getContext(), "Corso aggiornato con successo.").show();
                esci.callOnClick();
            }
            catch (Exception e) {
                displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    
    private String loadCorso() {
        String statoCorso = null;
        try {
            List<Row> rows = ConcreteDataAccessor.getInstance().read(Corso.TABLE_NAME, null, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso), null);

            for (Row row : rows) {
                _descrizione.setText(row.getColumnValue(Corso.corsoColumns.get(Corso.DESCRIZIONE)).toString());
                _dataInizioValidita.setText(dateFormat(row.getColumnValue(Corso.corsoColumns.get(Corso.DATA_INIZIO_VALIDITA)).toString(), "yyyy-MM-dd", "dd-MM-yyyy"));
                _dataFineValidita.setText(dateFormat(row.getColumnValue(Corso.corsoColumns.get(Corso.DATA_FINE_VALIDITA)).toString(), "yyyy-MM-dd", "dd-MM-yyyy"));

                statoCorso = row.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).toString();

                descrizioneCorso = _descrizione.getText().toString();
                dataInizioValidita = _dataInizioValidita.getText().toString();
                dataFineValidita = _dataFineValidita.getText().toString();
            }
        }
        catch (Exception e) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
        return statoCorso;
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Giorno", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Fascia oraria", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Capienza", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        headerTabellaFasceCorso.addView(tableRow);
    }

    public void loadFasceOrarie() {
        try {
            Row selectionColumn = new Row();
            selectionColumn.addColumn(FasciaCorso.fasceCorsoColumns.get(FasciaCorso.ID_CORSO), idCorso);
            if (oraInizioFascia != null && oraFineFascia != null) {
                selectionColumn.addColumn(FasciaCorso.fasceCorsoColumns.get(FasciaCorso.ORA_INIZIO), oraInizioFascia);
                selectionColumn.addColumn(FasciaCorso.fasceCorsoColumns.get(FasciaCorso.ORA_FINE), oraFineFascia);
            }

            String[] sortColumn = {FasciaCorso.fasceCorsoColumns.get(FasciaCorso.DESCRIZIONE_CORSO), FasciaCorso.fasceCorsoColumns.get(FasciaCorso.GIORNO_SETTIMANA)};
            List<Row> rows = ConcreteDataAccessor.getInstance().read(FasciaCorso.VIEW_FASCE_CORSO, null, selectionColumn, sortColumn);

            for (Row row : rows) {
                tableRow = new TableRow(this);
                tableRow.setClickable(true);

                tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE,
                        larghezzaColonna1,
                        row.getColumnValue(FasciaCorso.fasceCorsoColumns.get(FasciaCorso.GIORNO_SETTIMANA)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE,
                        larghezzaColonna2,
                        row.getColumnValue(FasciaCorso.fasceCorsoColumns.get(FasciaCorso.DESCRIZIONE_FASCIA)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE,
                        larghezzaColonna3,
                        row.getColumnValue(FasciaCorso.fasceCorsoColumns.get(FasciaCorso.CAPIENZA)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_END,
                        View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, 0,
                        row.getColumnValue(FasciaCorso.fasceCorsoColumns.get(FasciaCorso.ID_FASCIA)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.GONE));

                Map<String, String> intentMap = new ArrayMap<>();
                intentMap.put("idCorso", String.valueOf(idCorso));
                intentMap.put("descrizioneCorso", descrizioneCorso);

                listenerTableRow(this, ModificaFascia.class, "idFascia", intentMap, 3);
                tabellaFasceModCorso.addView(tableRow);

            }
        }
        catch (Exception e) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Lettura fasce corso fallita, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(modificaCorso, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione");
        messaggio.setMessage("Stai eliminando il corso : " + String.valueOf(descrizioneCorso) +
                "\nCancellerò anche tutti i dati legati a questo corso" + "\n\nConfermi?");
        messaggio.setCancelable(false);
        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    ConcreteDataAccessor.getInstance().delete(Corso.TABLE_NAME, new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso));
                    makeToastMessage(AgendaCorsiApp.getContext(), "Corso eliminato con successo.").show();
                    esci.callOnClick();
                }
                catch (Exception e) {
                    displayAlertDialog(modificaCorso, "Attenzione!", "Cancellazione fallita, contatta il supporto tecnico");
                }
            }
        }).getContext();

        messaggio.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog ad = messaggio.create();
        ad.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ModificaCorso.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
