package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.agendaCorsi.database.Fascia;
import com.example.agendaCorsi.database.FasciaDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.Map;

public class NuovaFascia extends FunctionBase {

    int idCorso;
    EditText _descrizione, _giornoSettimana, _oraInizio, _oraFine, _capienza;
    Context nuovaFascia;
    NumberPicker giornoSettimanaPicker, capienzaPicker;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_fascia);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        Intent intent = getIntent();
        idCorso = intent.getIntExtra("idCorso", 0);

        _descrizione = findViewById(R.id.editDescrizione);

        _oraInizio = findViewById(R.id.editOraInizio);
        TimePickerDialog.OnTimeSetListener oraInizio = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                _oraInizio.setText(String.valueOf(hourOfDay) + "." + String.valueOf(minute));
            }
        };

        _oraFine = findViewById(R.id.editOraFine);
        TimePickerDialog.OnTimeSetListener oraFine = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                _oraFine.setText(String.valueOf(hourOfDay) + "." + String.valueOf(minute));
            }
        };

        giornoSettimanaPicker = findViewById(R.id.editGiornoSettimana);
        giornoSettimanaPicker.setMinValue(1);
        giornoSettimanaPicker.setMaxValue(7);
        giornoSettimanaPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                _giornoSettimana.setText(newVal);
            }
        });

        capienzaPicker = findViewById(R.id.editCapienza);
        capienzaPicker.setMinValue(1);
        capienzaPicker.setMaxValue(50);
        capienzaPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                _capienza.setText(newVal);
            }
        });

        nuovaFascia = this;
        /*
         * Listener sui bottoni
         */
        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idCorso", String.valueOf(idCorso));

        listenerEsci(nuovaFascia, ModificaCorso.class, intentMap);
        listenerAnnulla();
        listenerSalva();
    }

    public void makeSalva() {
        Fascia fascia = new Fascia(null, String.valueOf(idCorso), _descrizione.getText().toString(),
                _giornoSettimana.getText().toString(),
                _oraInizio.getText().toString(),
                _oraFine.getText().toString(),
                _capienza.getText().toString(),
                null,
                null);

        if (fascia.getDescrizione().equals("") ||
            fascia.getOraInizio().equals("") ||
            fascia.getOraFine().equals("") ||
            fascia.getGiornoSettimana().equals("") ||
            fascia.getCapienza().equals("")) {

            displayAlertDialog(nuovaFascia, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            if (new FasciaDAO(nuovaFascia).isNew(fascia)) {
                if (new FasciaDAO(nuovaFascia).insert(fascia)) {
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(nuovaFascia, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
                }
            }
            else {
                displayAlertDialog(nuovaFascia, "Attenzione!", "Fascia sovrapposta non ammessa");
            }
        }
    }

    public void makeAnnulla() {
        _descrizione.setText("");
        _oraInizio.setText("");
        _oraFine.setText("");
        _giornoSettimana.setText("");
        _capienza.setText("");
    }
}
