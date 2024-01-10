package com.example.agendaCorsi.ui.corsi;

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
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ModificaCorso extends FunctionBase {

    String idCorso;
    RadioButton skate, basket, pattini, pallavolo;
    String descrizioneCorso, sport, dataInizioValidita, dataFineValidita;
    EditText _descrizione, _sport, _stato, _dataInizioValidita, _dataFineValidita;
    Context modificaCorso;
    TableLayout tabellaFasce;
    TextView descrizione, giorno_settimana, id_fascia, fascia_oraria;
    final Calendar myCalendar = Calendar.getInstance();

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

        skate = findViewById(R.id.radio_skate);
        basket = findViewById(R.id.radio_basket);
        pattini = findViewById(R.id.radio_pattini);
        pallavolo = findViewById(R.id.radio_pallavolo);

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
        skate.setChecked(true);
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
                null);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        if (new CorsoDAO(this).updateStato(corso, properties.getProperty(QUERY_MOD_STATO_CORSO))) {
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
                null);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        if (new CorsoDAO(this).updateStato(corso, properties.getProperty(QUERY_MOD_STATO_CORSO))) {
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
                null);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        if (new CorsoDAO(this).updateStato(corso, properties.getProperty(QUERY_MOD_STATO_CORSO))) {
            esci.callOnClick();
        }
        else {
            displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public void makeSalva() {
        Corso corso = new Corso(null,
                _descrizione.getText().toString(),
                _sport.getText().toString(),
                STATO_APERTO,
                dateFormat(_dataInizioValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),
                dateFormat(_dataFineValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),
                null,
                null);

        if (corso.getDescrizione().equals("") || corso.getDataInizioValidita().equals("") ||
            corso.getDataFineValidita().equals("")) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            propertyReader = new PropertyReader(this);
            properties = propertyReader.getMyProperties("config.properties");

            if (new CorsoDAO(this).update(corso, properties.getProperty(QUERY_MOD_CORSO))) {
                esci.callOnClick();
            }
            else {
                displayAlertDialog(modificaCorso, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    private void loadCorso() {
        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");
        /**
         * Carico i dati del corso selezionato
         */
        Corso corso = new Corso(String.valueOf(idCorso), null, null, null, null, null, null, null);
        new CorsoDAO(modificaCorso).select(corso, properties.getProperty(QUERY_GET_CORSO));

        if (corso.getIdCorso().equals("")) {
            displayAlertDialog(modificaCorso, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
        else {
            _descrizione.setText(corso.getDescrizione());
            _dataInizioValidita.setText(dateFormat(corso.getDataInizioValidita(), "yyyy-MM-dd", "dd-MM-yyyy"));
            _dataFineValidita.setText(dateFormat(corso.getDataFineValidita(), "yyyy-MM-dd", "dd-MM-yyyy"));

            if (corso.getSport().equals(Skate)) { skate.setChecked(true); }
            if (corso.getSport().equals(Basket)) { basket.setChecked(true); }
            if (corso.getSport().equals(Pattini)) { pattini.setChecked(true); }
            if (corso.getSport().equals(Pallavolo)) { pallavolo.setChecked(true); }

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

            loadFasceOrarie(idCorso, corso.getDescrizione());
        }
    }

    private void loadFasceOrarie(String idCorso, String descrizioneCorso) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.3);

        propertyReader = new PropertyReader(modificaCorso);
        properties = propertyReader.getMyProperties("config.properties");

        List<Object> fasceCorsoList = new FasciaDAO(modificaCorso).getFasceCorso(idCorso, properties.getProperty(QUERY_GET_FASCE_CORSO));

        for (Object object : fasceCorsoList) {
            Fascia fascia = Fascia.class.cast(object);

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            giorno_settimana = new TextView(modificaCorso);
            giorno_settimana.setTextSize(16);
            giorno_settimana.setPadding(10,20,10,20);
            giorno_settimana.setBackground(ContextCompat.getDrawable(ModificaCorso.this, R.drawable.cell_border));
            giorno_settimana.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            giorno_settimana.setGravity(Gravity.CENTER);
            giorno_settimana.setText(String.valueOf(fascia.getGiornoSettimana()));
            giorno_settimana.setWidth(larghezzaColonna1);
            tableRow.addView(giorno_settimana);

            descrizione = new TextView(modificaCorso);
            descrizione.setTextSize(16);
            descrizione.setPadding(10,20,10,20);
            descrizione.setBackground(ContextCompat.getDrawable(ModificaCorso.this, R.drawable.cell_border));
            descrizione.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            descrizione.setGravity(Gravity.CENTER);
            descrizione.setText(String.valueOf(fascia.getDescrizione()));
            descrizione.setWidth(larghezzaColonna2);
            tableRow.addView(descrizione);

            fascia_oraria = new TextView(modificaCorso);
            fascia_oraria.setTextSize(16);
            fascia_oraria.setPadding(10,20,10,20);
            fascia_oraria.setBackground(ContextCompat.getDrawable(ModificaCorso.this, R.drawable.cell_border));
            fascia_oraria.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            fascia_oraria.setGravity(Gravity.CENTER);
            fascia_oraria.setText(fascia.getOraInizio() + "-" + fascia.getOraFine());
            fascia_oraria.setWidth(larghezzaColonna3);
            tableRow.addView((fascia_oraria));

            id_fascia = new TextView(modificaCorso);
            id_fascia.setText(String.valueOf(fascia.getIdFascia()));
            id_fascia.setVisibility(View.INVISIBLE);
            tableRow.addView(id_fascia);

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("idCorso", String.valueOf(idCorso));
            intentMap.put("descrizioneCorso", descrizioneCorso);

            listenerTableRow(modificaCorso, ModificaFascia.class, "idFascia", intentMap, 3);
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

        propertyReader = new PropertyReader(modificaCorso);
        properties = propertyReader.getMyProperties("config.properties");
        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Corso corso = new Corso(String.valueOf(idCorso), null, null, null, null, null, null, null);
                if (new CorsoDAO(modificaCorso).delete(corso, properties.getProperty(QUERY_DEL_CORSO))) {
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
