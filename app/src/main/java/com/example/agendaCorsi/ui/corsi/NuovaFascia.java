package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.Map;

public class NuovaFascia extends FunctionBase {

    String idCorso;
    EditText _descrizione, _oraInizio, _oraFine, _giornoSettimana, _capienza;
    Context nuovaFascia;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_fascia);

        nuovaFascia = this;

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        Intent intent = getIntent();
        idCorso = intent.getStringExtra("idCorso");

        _descrizione = findViewById(R.id.editDescrizione);

        _oraInizio = findViewById(R.id.editOraInizio);
        _oraInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NuovaFascia.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        _oraInizio.setText(leftPad(String.valueOf(selectedHour), 2, "0") + "." + leftPad(String.valueOf(selectedMinute), 2, "0"));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Ora di inizio lezione");
                mTimePicker.show();
            }
        });

        _oraFine = findViewById(R.id.editOraFine);
        _oraFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NuovaFascia.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        _oraFine.setText(leftPad(String.valueOf(selectedHour), 2, "0") + "." + leftPad(String.valueOf(selectedMinute), 2, "0"));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Ora di fine lezione");
                mTimePicker.show();
            }
        });

        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _capienza = findViewById(R.id.editCapienza);

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
        Fascia fascia = new Fascia("", String.valueOf(idCorso), _descrizione.getText().toString(),
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
            if (isNumberOfWeek(Integer.parseInt(fascia.getGiornoSettimana()))) {
                if (FasciaDAO.getInstance().isNew(fascia, QueryComposer.getInstance().getQuery(QUERY_ISNEW_FASCIA))) {
                    if (FasciaDAO.getInstance().insert(fascia, QueryComposer.getInstance().getQuery(QUERY_INS_FASCIA))) {
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
            else {
                displayAlertDialog(nuovaFascia, "Attenzione!", "Giorno settimana non valido");
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
