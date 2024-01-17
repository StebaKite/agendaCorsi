package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.table.FasciaCorso;
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
    TextView capienza, giorno_settimana, id_fascia, fascia_oraria, scrollViewTabellaFasce;
    final Calendar myCalendar = Calendar.getInstance();

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_corso);
        tabellaFasce = findViewById(R.id.tabellaFasce);

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

        loadCorso();
        /*
         * Set dei listeners sui bottoni
         */
        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idCorso", String.valueOf(idCorso));
        intentMap.put("descrizioneCorso", descrizioneCorso);

        listenerChiudi();
        listenerSospendi();
        listenerApri();
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
        Toast.makeText(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito", Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeApri() {
        Corso corso = new Corso(String.valueOf(idCorso),
                null,
                null,
                STATO_APERTO,
                null,
                null,
                null,
                null,
                null);

        if (CorsoDAO.getInstance().updateStato(corso, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_CORSO))) {
            Toast.makeText(AgendaCorsiApp.getContext(), "Corso aperto con successo", Toast.LENGTH_LONG).show();
            esci.callOnClick();
        }
        else {
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
            Toast.makeText(AgendaCorsiApp.getContext(), "Corso sospeso con successo", Toast.LENGTH_LONG).show();
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
            Toast.makeText(AgendaCorsiApp.getContext(), "Corso chiuso con successo", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AgendaCorsiApp.getContext(), "Corso aggiornato con successo.", Toast.LENGTH_LONG).show();
                esci.callOnClick();
            }
            else {
                displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    private void loadCorso() {
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

            /*
             * La visibilità dei bottoni di cambio stato rispetta le regole
             * documentate dal diagramma degli stati del Corso
             */
            if (corso.getStato().equals(STATO_CHIUSO)) {
                chiudi.setVisibility(View.INVISIBLE);
                sospendi.setVisibility(View.INVISIBLE);
                apri.setVisibility(View.INVISIBLE);
            } else if (corso.getStato().equals(STATO_APERTO)) {
                apri.setVisibility(View.INVISIBLE);
            } else if (corso.getStato().equals(STATO_SOSPESO)) {
                chiudi.setVisibility(View.INVISIBLE);
                sospendi.setVisibility(View.INVISIBLE);
            } else if (corso.getStato().equals(STATO_ATTIVO)) {
                apri.setVisibility(View.INVISIBLE);
            }

            loadFasceOrarie();
        }
    }

    public void loadFasceOrarie() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.1);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);
        int larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.1);

        List<Object> fasceCorsoList = FasciaDAO.getInstance().getFasceCorso(idCorso, QueryComposer.getInstance().getQuery(QUERY_GET_FASCE_CORSO));

        for (Object object : fasceCorsoList) {
            FasciaCorso fasciaCorso = (FasciaCorso) object;

            tableRow = new TableRow(AgendaCorsiApp.getContext());
            tableRow.setClickable(true);

            TextView giorno_settimana = new TextView(AgendaCorsiApp.getContext());
            giorno_settimana.setTextSize(16);
            giorno_settimana.setPadding(10,20,10,20);
            giorno_settimana.setBackground(ContextCompat.getDrawable(AgendaCorsiApp.getContext(), R.drawable.cell_border));
            giorno_settimana.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            giorno_settimana.setGravity(Gravity.CENTER);
            giorno_settimana.setText(String.valueOf(fasciaCorso.getGiornoSettimana()));
            giorno_settimana.setWidth(larghezzaColonna1);
            tableRow.addView(giorno_settimana);

            TextView fascia_oraria = new TextView(AgendaCorsiApp.getContext());
            fascia_oraria.setTextSize(16);
            fascia_oraria.setPadding(10,20,10,20);
            fascia_oraria.setBackground(ContextCompat.getDrawable(AgendaCorsiApp.getContext(), R.drawable.cell_border));
            fascia_oraria.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            fascia_oraria.setGravity(Gravity.CENTER);
            fascia_oraria.setText(fasciaCorso.getDescrizioneFascia());
            fascia_oraria.setWidth(larghezzaColonna2);
            tableRow.addView((fascia_oraria));

            TextView capienza = new TextView(AgendaCorsiApp.getContext());
            capienza.setTextSize(16);
            capienza.setPadding(10,20,10,20);
            capienza.setBackground(ContextCompat.getDrawable(AgendaCorsiApp.getContext(), R.drawable.cell_border));
            capienza.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            capienza.setGravity(Gravity.CENTER);
            capienza.setText(fasciaCorso.getCapienza());
            capienza.setWidth(larghezzaColonna3);
            tableRow.addView((capienza));

            TextView id_fascia = new TextView(AgendaCorsiApp.getContext());
            id_fascia.setText(String.valueOf(fasciaCorso.getIdFascia()));
            id_fascia.setVisibility(View.INVISIBLE);
            tableRow.addView(id_fascia);

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("idCorso", String.valueOf(idCorso));
            intentMap.put("descrizioneCorso", descrizioneCorso);

            listenerTableRow(AgendaCorsiApp.getContext(), ModificaFascia.class, "idFascia", intentMap, 3);
            tabellaFasce.addView(tableRow);
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
                    Toast.makeText(AgendaCorsiApp.getContext(), "Corso eliminato con successo.", Toast.LENGTH_LONG).show();
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
}
