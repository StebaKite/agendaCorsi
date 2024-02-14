package com.example.agendaCorsi.ui.corsi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NuovoCorso extends FunctionBase {

    RadioButton skate, basket, pattini, pallavolo, normale, aperto;
    TextView descrizione, sport;
    EditText dataInizioValidita, dataFineValidita;
    Context nuovoCorso;
    final Calendar myCalendar= Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_corso);

        makeToolBar(this);

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
                updateLabel(dataInizioValidita, myCalendar);
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
                updateLabel(dataFineValidita, myCalendar);
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

        normale = findViewById(R.id.radio_normale);
        aperto = findViewById(R.id.radio_aperto);

        nuovoCorso = this;
        /**
         * Listener sui bottoni
         */
        listenerEsci(nuovoCorso, ElencoCorsi.class, null);
        listenerAnnulla();
        listenerSalva();
    }


    public void makeSalva() {
        if (descrizione.getText().toString().equals("") ||
            dataInizioValidita.getText().toString().equals("") ||
            dataFineValidita.getText().toString().equals("")) {

            displayAlertDialog(nuovoCorso, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();

                String sport = "";
                if (skate.isChecked()) {
                    sport = Skate;
                }
                if (basket.isChecked()) {
                    sport = Basket;
                }
                if (pattini.isChecked()) {
                    sport = Pattini;
                }
                if (pallavolo.isChecked()) {
                    sport = Pallavolo;
                }

                String tipo = "";
                if (normale.isChecked()) {
                    tipo = Produzione;
                }
                if (aperto.isChecked()) {
                    tipo = Test;
                }

                Row insertColumn = new Row();
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.DESCRIZIONE), descrizione.getText().toString());
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.SPORT), sport);
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.STATO), STATO_APERTO);
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.TIPO), tipo);
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.DATA_INIZIO_VALIDITA), dateFormat(dataInizioValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.DATA_FINE_VALIDITA), dateFormat(dataFineValidita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.DATA_CREAZIONE), dateFormat.format(date));
                insertColumn.addColumn(Corso.corsoColumns.get(Corso.DATA_ULTIMO_AGGIORNAMENTO), dateFormat.format(date));

                List<Row> rowToInsert = new LinkedList<>();
                rowToInsert.add(insertColumn);

                ConcreteDataAccessor.getInstance().insert(Corso.TABLE_NAME, rowToInsert);
                makeToastMessage(AgendaCorsiApp.getContext(), "Corso creato con successo.").show();
                esci.callOnClick();
            }
            catch (Exception e) {
                displayAlertDialog(nuovoCorso, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
            }
        }
    }

    public void makeAnnulla() {
        descrizione.setText("");
        dataInizioValidita.setText("");
        dataFineValidita.setText("");
        skate.setChecked(true);
        normale.setChecked(true);
        makeToastMessage(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito").show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(NuovoCorso.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
