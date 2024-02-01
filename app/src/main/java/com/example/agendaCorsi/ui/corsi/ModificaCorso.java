package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.access.TotaleCorsoDAO;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.database.table.TotaleIscrizioniCorso;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ModificaCorso extends FunctionBase {

    String idCorso;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

        headerTabellaFasceCorso = findViewById(R.id.headerTabellaContattiIscrivibili);
        tabellaFasceModCorso = findViewById(R.id.tabellaFasceModCorso);

        Intent intent = getIntent();
        idCorso = intent.getStringExtra("idCorso");

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

        Corso corso = loadCorso();
        testataElenco();
        loadFasceOrarie();

        /*
         * La visibilità dei bottoni di cambio stato rispetta le regole
         * documentate dal diagramma degli stati del Corso
         */
        if (corso.getStato().equals(STATO_CHIUSO)) {
            chiudi.setVisibility(View.GONE);
            sospendi.setVisibility(View.GONE);
            apri.setVisibility(View.GONE);
        } else if (corso.getStato().equals(STATO_APERTO)) {
            apri.setVisibility(View.GONE);
            listenerChiudi();
            listenerSospendi();
        } else if (corso.getStato().equals(STATO_SOSPESO)) {
            chiudi.setVisibility(View.GONE);
            sospendi.setVisibility(View.GONE);
            listenerApri();
        } else if (corso.getStato().equals(STATO_ATTIVO)) {
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
        Corso corso = new Corso(String.valueOf(idCorso),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        TotaleIscrizioniCorso totaleIscrizioniCorso = new TotaleIscrizioniCorso(idCorso, null, null, null);
        TotaleCorsoDAO.getInstance().selectTotaleIscrizioni(totaleIscrizioniCorso, QueryComposer.getInstance().getQuery(QUERY_TOT_ISCRIZIONI));
        if (totaleIscrizioniCorso.getDescrizioneCorso() != null) {
            if (Integer.parseInt(totaleIscrizioniCorso.getTotaleIscrizioni()) > 0) {
                corso.setStato(STATO_ATTIVO);
            } else {
                corso.setStato(STATO_APERTO);
            }
        } else {
            corso.setStato(STATO_APERTO);
        }

        if (CorsoDAO.getInstance().updateStato(corso, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_CORSO))) {
            makeToastMessage(AgendaCorsiApp.getContext(), "Corso aperto con successo").show();
            esci.callOnClick();
        } else {
            displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeSospendi() {
        Corso corso = new Corso(String.valueOf(idCorso),
                null,
                null,
                STATO_SOSPESO,
                null,
                null,
                null,
                null,
                null);

        if (CorsoDAO.getInstance().updateStato(corso, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_CORSO))) {
            makeToastMessage(AgendaCorsiApp.getContext(), "Corso sospeso con successo").show();
            esci.callOnClick();
        }
        else {
            displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeChiudi() {
        Corso corso = new Corso(String.valueOf(idCorso),
                null,
                null,
                STATO_CHIUSO,
                null,
                null,
                null,
                null,
                null);

        if (CorsoDAO.getInstance().updateStato(corso, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_CORSO))) {
            makeToastMessage(AgendaCorsiApp.getContext(), "Corso chiuso con successo").show();
            esci.callOnClick();
        }
        else {
            displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeSalva() {
        Corso corso = new Corso(idCorso,
                _descrizione.getText().toString(),
                null,
                STATO_APERTO,
                null,
                dateFormat(_dataInizioValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),
                dateFormat(_dataFineValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),
                null,
                null);

        if (corso.getDescrizione().equals("") || corso.getDataInizioValidita().equals("") ||
            corso.getDataFineValidita().equals("")) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            if (CorsoDAO.getInstance().update(corso, QueryComposer.getInstance().getQuery(QUERY_MOD_CORSO))) {
                makeToastMessage(AgendaCorsiApp.getContext(), "Corso aggiornato con successo.").show();
                esci.callOnClick();
            }
            else {
                displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    private Corso loadCorso() {
        /**
         * Carico i dati del corso selezionato
         */
        Corso corso = new Corso(String.valueOf(idCorso), null, null, null, null, null, null, null, null);
        CorsoDAO.getInstance().select(corso, QueryComposer.getInstance().getQuery(QUERY_GET_CORSO));

        if (corso.getIdCorso().equals("")) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
        else {
            _descrizione.setText(corso.getDescrizione());
            _dataInizioValidita.setText(dateFormat(corso.getDataInizioValidita(), "yyyy-MM-dd", "dd-MM-yyyy"));
            _dataFineValidita.setText(dateFormat(corso.getDataFineValidita(), "yyyy-MM-dd", "dd-MM-yyyy"));

            descrizioneCorso = _descrizione.getText().toString();
            dataInizioValidita = _dataInizioValidita.getText().toString();
            dataFineValidita = _dataFineValidita.getText().toString();
        }
        return corso;
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
        List<Object> fasceCorsoList = FasciaDAO.getInstance().getFasceCorso(idCorso, QueryComposer.getInstance().getQuery(QUERY_GET_FASCE_CORSO));

        for (Object object : fasceCorsoList) {
            FasciaCorso fasciaCorso = (FasciaCorso) object;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna1, fasciaCorso.getGiornoSettimana(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna2, fasciaCorso.getDescrizioneFascia(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, larghezzaColonna3, fasciaCorso.getCapienza(), View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL_SIMPLE, 0, fasciaCorso.getIdFascia(), View.TEXT_ALIGNMENT_TEXT_START, View.GONE));

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("idCorso", String.valueOf(idCorso));
            intentMap.put("descrizioneCorso", descrizioneCorso);

            listenerTableRow(this, ModificaFascia.class, "idFascia", intentMap, 3);
            tabellaFasceModCorso.addView(tableRow);
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
                Corso corso = new Corso(String.valueOf(idCorso), null, null, null, null, null, null, null, null);
                if (CorsoDAO.getInstance().delete(corso, QueryComposer.getInstance().getQuery(QUERY_DEL_CORSO))) {
                    makeToastMessage(AgendaCorsiApp.getContext(), "Corso eliminato con successo.").show();
                    esci.callOnClick();
                }
                else {
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
