package com.example.agendaCorsi.ui.corsi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.ContattiDAO;
import com.example.agendaCorsi.database.Contatto;
import com.example.agendaCorsi.database.Corso;
import com.example.agendaCorsi.database.CorsoDAO;
import com.example.agendaCorsi.database.Fascia;
import com.example.agendaCorsi.database.FasciaDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.contatti.ModificaContatto;
import com.example.agendaCorsi.ui.contatti.ModificaElementoPortfolio;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModificaCorso extends FunctionBase {

    int idCorso;
    Button annulla, esci, elimina, salva;
    RadioButton skate, basket, pattini, pallavolo;
    String descrizioneCorso, sport, dataInizioValidita, dataFineValidita;
    EditText _descrizione, _sport, _dataInizioValidita, _dataFineValidita;
    Context modificaCorso;
    TableLayout tabellaFasce;
    TableRow tableRow;
    TextView descrizione, giorno_settimana, id_fascia;
    final Calendar myCalendar= Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_corso);
        tabellaFasce = findViewById(R.id.tabellaFasce);

        Intent intent = getIntent();
        idCorso = intent.getIntExtra("idCorso", 0);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        elimina = findViewById(R.id.bElimina);
        salva = findViewById(R.id.bSalva);

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
                updateLabel(_dataInizioValidita);
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
                updateLabel(_dataFineValidita);
            }
        };
        _dataFineValidita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ModificaCorso.this, dataFine, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        _descrizione = findViewById(R.id.editDescrizione);
        _dataInizioValidita = findViewById(R.id.editDataInizio);
        _dataFineValidita = findViewById(R.id.editDataFine);
        skate = findViewById(R.id.radio_skate);
        basket = findViewById(R.id.radio_basket);
        pattini = findViewById(R.id.radio_pattini);
        pallavolo = findViewById(R.id.radio_pallavolo);

        modificaCorso = this;

        loadCorso();

        /**
         * Listener sui bottoni
         */
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModificaCorso.this, ElencoCorsi.class);
                startActivity(intent);
            }
        });

        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _descrizione.setText(descrizioneCorso);
                _dataInizioValidita.setText(dataInizioValidita);
                _dataFineValidita.setText(dataFineValidita);
                skate.setChecked(true);
            }
        });

        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeElimina();
            }
        });


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSalva();
            }
        });
    }

    private void makeSalva() {
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
            if (new CorsoDAO(this).update(corso)) {
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
        Corso corso = new Corso(String.valueOf(idCorso), null, null, null, null, null, null, null);
        new CorsoDAO(modificaCorso).select(corso);

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

            loadFasceOrarie(idCorso, corso.getDescrizione());
        }
    }

    private void loadFasceOrarie(int idCorso, String descrizioneCorso) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.5);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        List<Object> fasceCorsoList = new FasciaDAO(this).getFasceCorso(idCorso);

        for (Object object : fasceCorsoList) {
            Fascia fascia = Fascia.class.cast(object);

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            descrizione = new TextView(this);
            descrizione.setTextSize(16);
            descrizione.setPadding(10,20,10,20);
            descrizione.setBackground(ContextCompat.getDrawable(ModificaCorso.this, R.drawable.cell_border));
            descrizione.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            descrizione.setGravity(Gravity.CENTER);
            descrizione.setText(String.valueOf(fascia.getDescrizione()));
            descrizione.setWidth(larghezzaColonna1);
            tableRow.addView(descrizione);

            giorno_settimana = new TextView(this);
            giorno_settimana.setTextSize(16);
            giorno_settimana.setPadding(10,20,10,20);
            giorno_settimana.setBackground(ContextCompat.getDrawable(ModificaCorso.this, R.drawable.cell_border));
            giorno_settimana.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            giorno_settimana.setGravity(Gravity.CENTER);
            giorno_settimana.setText(String.valueOf(fascia.getGiornoSettimana()));
            giorno_settimana.setWidth(larghezzaColonna1);
            tableRow.addView(giorno_settimana);

            id_fascia = new TextView(this);
            id_fascia.setText(String.valueOf(fascia.getIdFascia()));
            id_fascia.setVisibility(View.INVISIBLE);
            tableRow.addView(id_fascia);

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;        // in view c'è la riga selezionata
                    TextView textView = (TextView) tableRow.getChildAt(2);    // id_elemento
                    Integer idFasciaSelezionata = Integer.parseInt(textView.getText().toString());

                    /**
                     * Passo all'attività ModificaFascia l'id della fascia selezionata
                     */
                    Intent intent = new Intent(ModificaCorso.this, ModificaFascia.class);
                    intent.putExtra("idFascia", idFasciaSelezionata);
                    intent.putExtra("idCorso", idCorso);
                    intent.putExtra("descrizioneCorso", descrizioneCorso);
                    startActivity(intent);
                }
            });
            /**
             * aggiungo la riga alla tabella degli elementi portfolio
             */
            tabellaFasce.addView(tableRow);
        }
    }

    private void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaCorso.this, R.style.Theme_InfoDialog);
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
                Corso corso = new Corso(String.valueOf(idCorso), null, null, null, null, null, null, null);
                if (new CorsoDAO(modificaCorso).delete(corso)) {
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

    private void updateLabel(EditText dataInizioValidita){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dataInizioValidita.setText(dateFormat.format(myCalendar.getTime()));
    }
}