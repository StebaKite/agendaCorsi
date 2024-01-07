package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.Map;

public class ModificaFascia extends FunctionBase {

    int idCorso, idFascia;
    EditText _descrizione, _giornoSettimana, _oraInizio, _oraFine, _capienza;
    Context modificaFascia;
    NumberPicker giornoSettimanaPicker, capienzaPicker;
    TextView lScheda;
    String desrizioneCorso, descrizione, giornoSettimana, oraInizio, oraFine, capienza;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_fascia);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        lScheda = findViewById(R.id.lScheda);

        Intent intent = getIntent();
        idCorso = intent.getIntExtra("idCorso", 0);
        idFascia = intent.getIntExtra("idFascia", 0);
        desrizioneCorso = intent.getStringExtra("descrizioneCorso");

        lScheda.setText("Modifica fascia " + desrizioneCorso);

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

        modificaFascia = this;

        loadFascia();
        /*
         * Listener sui bottoni
         */
        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idCorso", String.valueOf(idCorso));

        listenerEsci(modificaFascia, ModificaCorso.class, intentMap);
        listenerAnnulla();
        listenerSalva();
        listenerElimina();
    }

    private void loadFascia() {
        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");
        /*
         * Carico i dati della fascia selezionata
         */
        Fascia fascia = new Fascia(String.valueOf(idFascia), String.valueOf(idCorso),null,null, null, null, null,null,null);
        new FasciaDAO(modificaFascia).select(fascia, properties.getProperty(QUERY_GET_FASCIA));

        if (fascia.getIdFascia().equals("")) {
            displayAlertDialog(modificaFascia, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
        else {
            _descrizione.setText(fascia.getDescrizione());
            _giornoSettimana.setText(fascia.getGiornoSettimana());
            _oraInizio.setText(fascia.getOraInizio());
            _oraFine.setText(fascia.getOraFine());
            _capienza.setText(fascia.getCapienza());

            descrizione = _descrizione.getText().toString();
            giornoSettimana = _descrizione.getText().toString();
            oraInizio = _oraInizio.getText().toString();
            oraFine = _oraFine.getText().toString();
            capienza = _capienza.getText().toString();
        }
    }

    public void makeAnnulla() {
        _descrizione.setText(descrizione);
        _giornoSettimana.setText(giornoSettimana);
        _oraInizio.setText(oraInizio);
        _oraFine.setText(oraFine);
        _capienza.setText(capienza);
    }

    public void makeSalva() {
        Fascia fascia = new Fascia(String.valueOf(idFascia), String.valueOf(idCorso), _descrizione.getText().toString(),
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

            displayAlertDialog(modificaFascia, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            propertyReader = new PropertyReader(modificaFascia);
            properties = propertyReader.getMyProperties("config.properties");

            if (new FasciaDAO(modificaFascia).isNew(fascia, properties.getProperty(QUERY_ISNEW_FASCIA))) {
                if (new FasciaDAO(modificaFascia).insert(fascia, properties.getProperty(QUERY_MOD_FASCIA))) {
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(modificaFascia, "Attenzione!", "Modifica fallita, contatta il supporto tecnico");
                }
            }
            else {
                displayAlertDialog(modificaFascia, "Attenzione!", "Fascia sovrapposta non ammessa");
            }
        }
    }

    public void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(modificaFascia, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione");
        messaggio.setMessage("Stai eliminando la fascia : " + String.valueOf(descrizione) +
                "\nCancellerò anche tutti i dati legati a questa fascia oraria." + "\n\nConfermi?");
        messaggio.setCancelable(false);

        propertyReader = new PropertyReader(modificaFascia);
        properties = propertyReader.getMyProperties("config.properties");
        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fascia fascia = new Fascia(String.valueOf(idFascia), null, null, null, null, null, null, null, null);
                if (new FasciaDAO(modificaFascia).delete(fascia, properties.getProperty(QUERY_DEL_FASCIA))) {
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(modificaFascia, "Attenzione!", "Cancellazione fallita, contatta il supporto tecnico");
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
