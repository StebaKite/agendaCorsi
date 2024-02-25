package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.CornerPathEffect;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ModificaFascia extends FunctionBase {

    String idCorso;
    String idFascia;
    EditText _descrizione, _giornoSettimana, _oraInizio, _oraFine, _capienza;
    Context modificaFascia;
    TextView lScheda;
    String desrizioneCorso, descrizione, giornoSettimana, oraInizio, oraFine, capienza;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_fascia);

        makeToolBar(this);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);
        elimina = findViewById(R.id.bElimina);

        lScheda = findViewById(R.id.lScheda);

        Intent intent = getIntent();
        idCorso = intent.getStringExtra("idCorso");
        idFascia = intent.getStringExtra("idFascia");
        desrizioneCorso = intent.getStringExtra("descrizioneCorso");

        lScheda.setText("Modifica fascia " + desrizioneCorso);

        _descrizione = findViewById(R.id.editDescrizione);

        _oraInizio = findViewById(R.id.editOraInizio);
        _oraInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ModificaFascia.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        _oraInizio.setText(selectedHour + "." + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
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
                mTimePicker = new TimePickerDialog(ModificaFascia.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        _oraFine.setText(selectedHour + "." + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Ora di fine lezione");
                mTimePicker.show();
            }
        });

        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _capienza = findViewById(R.id.editCapienza);

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
        try {
            List<Row> rows = ConcreteDataAccessor.getInstance().read(Fascia.TABLE_NAME, null, new Row(Fascia.fasciaColumns.get(Fascia.ID_FASCIA), idFascia), null);
            for (Row row : rows) {
                _descrizione.setText(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.DESCRIZIONE)).toString());
                _giornoSettimana.setText(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.GIORNO_SETTIMANA)).toString());
                _oraInizio.setText(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO)).toString());
                _oraFine.setText(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_FINE)).toString());
                _capienza.setText(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.CAPIENZA)).toString());

                descrizione = _descrizione.getText().toString();
                giornoSettimana = _giornoSettimana.getText().toString();
                oraInizio = _oraInizio.getText().toString();
                oraFine = _oraFine.getText().toString();
                capienza = _capienza.getText().toString();
            }
        }
        catch (Exception e) {
            displayAlertDialog(modificaFascia, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
    }

    public void makeAnnulla() {
        _descrizione.setText(descrizione);
        _giornoSettimana.setText(giornoSettimana);
        _oraInizio.setText(oraInizio);
        _oraFine.setText(oraFine);
        _capienza.setText(capienza);
        makeToastMessage(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito").show();
    }

    public void makeSalva() {
        if (_descrizione.getText().toString().equals("") ||
            _oraInizio.getText().toString().equals("") ||
            _oraFine.getText().toString().equals("") ||
            _giornoSettimana.getText().toString().equals("") ||
            _capienza.getText().toString().equals("")) {

            displayAlertDialog(modificaFascia, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            if (isNumberOfWeek(Integer.parseInt(_giornoSettimana.getText().toString()))) {
                if (isFasciaNonSovrapposta(idCorso, idFascia, giornoSettimana, _oraInizio.getText().toString(), _oraFine.getText().toString())) {
                    if (Integer.parseInt(String.valueOf(_capienza.getText())) >= Integer.parseInt(capienza)) {
                        try {
                            Row updateColumn = new Row();
                            boolean anyChange = false;

                            if (!_capienza.getText().toString().equals(capienza)) {
                                anyChange = true;
                                updateColumn.addColumn(Fascia.fasciaColumns.get(Fascia.CAPIENZA), _capienza.getText().toString());
                            }
                            if (!_oraFine.getText().toString().equals(oraFine)) {
                                anyChange = true;
                                updateColumn.addColumn(Fascia.fasciaColumns.get(Fascia.ORA_FINE), _oraFine.getText().toString());
                            }
                            if (!_oraInizio.getText().toString().equals(oraInizio)) {
                                anyChange = true;
                                updateColumn.addColumn(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO), _oraInizio.getText().toString());
                            }
                            if (!_giornoSettimana.getText().toString().equals(giornoSettimana)) {
                                anyChange = true;
                                updateColumn.addColumn(Fascia.fasciaColumns.get(Fascia.GIORNO_SETTIMANA), _giornoSettimana.getText().toString());
                            }
                            if (!_descrizione.getText().toString().equals(descrizione)) {
                                anyChange = true;
                                updateColumn.addColumn(Fascia.fasciaColumns.get(Fascia.DESCRIZIONE), _descrizione.getText().toString());
                            }

                            if (anyChange) {
                                updateColumn.addColumn(Fascia.fasciaColumns.get(Fascia.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());
                                ConcreteDataAccessor.getInstance().update(Fascia.TABLE_NAME, new Row(Fascia.fasciaColumns.get(Fascia.ID_FASCIA), idFascia), updateColumn);
                                makeToastMessage(AgendaCorsiApp.getContext(), "Fascia aggiornata con successo.").show();
                                esci.callOnClick();
                            }
                        } catch (Exception e) {
                            displayAlertDialog(modificaFascia, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                        }
                    } else {
                        displayAlertDialog(modificaFascia, "Attenzione!", "Riduzione capienza fascia non permessa");
                    }
                } else {
                    displayAlertDialog(modificaFascia, "Attenzione!", "Fascia sovrapposta non ammessa");
                }
            } else {
                displayAlertDialog(modificaFascia, "Attenzione!", "Giorno settimana non valido");
            }
        }
    }


    public void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(modificaFascia, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione");
        messaggio.setMessage("Stai eliminando la fascia : " + String.valueOf(descrizione) +
                "\nCancellerò anche tutti i dati legati a questa fascia oraria." + "\n\nConfermi?");
        messaggio.setCancelable(false);

        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fascia fascia = new Fascia(String.valueOf(idFascia), null, null, null, null, null, null, null, null);
                if (FasciaDAO.getInstance().delete(fascia, QueryComposer.getInstance().getQuery(QUERY_DEL_FASCIA))) {
                    makeToastMessage(AgendaCorsiApp.getContext(), "Fascia eliminatas con successo.").show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ModificaFascia.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
