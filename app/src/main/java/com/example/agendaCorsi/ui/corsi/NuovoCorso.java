package com.example.agendaCorsi.ui.corsi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.agendaCorsi.database.Corso;
import com.example.agendaCorsi.database.CorsoDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.Locale;

public class NuovoCorso extends FunctionBase {

    Button annulla, esci, salva;
    RadioButton skate, basket, pattini, pallavolo;
    TextView descrizione, sport;
    EditText dataInizioValidita, dataFineValidita;
    Context nuovoCorso;
    final Calendar myCalendar= Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_corso);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        descrizione = findViewById(R.id.editDescrizione);

        /**
         * Date picker data inizio validità
         */
        dataInizioValidita = findViewById(R.id.editDataInizio);

        DatePickerDialog.OnDateSetListener dataInizio = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(dataInizioValidita);
            }
        };
        dataInizioValidita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NuovoCorso.this, dataInizio, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**
         * Date picker data fine validità
         */
        dataFineValidita = findViewById(R.id.editDataFine);

        DatePickerDialog.OnDateSetListener dataFine = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(dataFineValidita);
            }
        };
        dataFineValidita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NuovoCorso.this, dataFine, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        descrizione = findViewById(R.id.editDescrizione);
        dataInizioValidita = findViewById(R.id.editDataInizio);
        dataFineValidita = findViewById(R.id.editDataFine);
        skate = findViewById(R.id.radio_skate);
        basket = findViewById(R.id.radio_basket);
        pattini = findViewById(R.id.radio_pattini);
        pallavolo = findViewById(R.id.radio_pallavolo);

        nuovoCorso = this;
        /**
         * Listener sui bottoni
         */
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuovoCorso.this, ElencoCorsi.class);
                startActivity(intent);
            }
        });

        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descrizione.setText("");
                dataInizioValidita.setText("");
                dataFineValidita.setText("");
                skate.setChecked(true);
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sport = "";
                if (skate.isChecked()) { sport = Skate; }
                if (basket.isChecked()) { sport = Basket; }
                if (pattini.isChecked()) { sport = Pattini; }
                if (pallavolo.isChecked()) { sport = Pallavolo; }

                Corso corso = new Corso(null, descrizione.getText().toString(),
                        sport, STATO_APERTO,
                        dateFormat(dataInizioValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),
                        dateFormat(dataFineValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"), null, null);

                if (corso.getDescrizione().equals("") ||
                    corso.getDataInizioValidita().equals("") ||
                    corso.getDataFineValidita().equals("") ||
                    corso.getSport().equals("")) {

                    displayAlertDialog(nuovoCorso, "Attenzione!", "Inserire tutti i campi");
                }
                else {
                    if (new CorsoDAO(nuovoCorso).insert(corso)) {
                        esci.callOnClick();
                    }
                    else {
                        displayAlertDialog(nuovoCorso, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
                    }
                }
            }
        });
    }

    private void updateLabel(EditText dataInizioValidita){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dataInizioValidita.setText(dateFormat.format(myCalendar.getTime()));
    }
}