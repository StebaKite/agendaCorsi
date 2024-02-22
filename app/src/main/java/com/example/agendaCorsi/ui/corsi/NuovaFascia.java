package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
        makeToolBar(nuovaFascia);

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
                mTimePicker = new TimePickerDialog(NuovaFascia.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        _oraFine.setText(leftPad(String.valueOf(selectedHour), 2, "0") + "." + leftPad(String.valueOf(selectedMinute), 2, "0"));
                    }
                }, hour, minute, false);//Yes 24 hour time
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
        if (_descrizione.getText().toString().equals("") ||
            _oraInizio.getText().toString().equals("") ||
            _oraFine.getText().toString().equals("") ||
            _giornoSettimana.getText().toString().equals("") ||
            _capienza.getText().toString().equals("")) {

            displayAlertDialog(nuovaFascia, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            try {
                if (isNumberOfWeek(Integer.parseInt(_giornoSettimana.getText().toString()))) {
                    if (isNewFascia()) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();

                        Row row = new Row();
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.ID_CORSO), idCorso);
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.DESCRIZIONE), _descrizione.getText().toString());
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.GIORNO_SETTIMANA), _giornoSettimana.getText().toString());
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO), _oraInizio.getText().toString());
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.ORA_FINE), _oraFine.getText().toString());
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.CAPIENZA), _capienza.getText().toString());
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.DATA_CREAZIONE), dateFormat.format(date));
                        row.addColumn(Fascia.fasciaColumns.get(Fascia.DATA_ULTIMO_AGGIORNAMENTO), dateFormat.format(date));

                        List<Row> insertRows = new LinkedList<>();
                        insertRows.add(row);

                        ConcreteDataAccessor.getInstance().insert(Fascia.TABLE_NAME, insertRows);
                        makeToastMessage(AgendaCorsiApp.getContext(), "Fascia creata con successo.").show();
                        esci.callOnClick();
                    }
                    else {
                        displayAlertDialog(nuovaFascia, "Attenzione!", "Fascia sovrapposta non ammessa");
                    }
                }
                else {
                    displayAlertDialog(nuovaFascia, "Attenzione!", "Giorno settimana non valido");
                }
            }
            catch (Exception e) {
                displayAlertDialog(nuovaFascia, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
            }
        }
    }

    private boolean isNewFascia() throws Exception {
        Row selectionRow = new Row();
        selectionRow.addColumn(Fascia.fasciaColumns.get(Fascia.ID_CORSO), idCorso);
        selectionRow.addColumn(Fascia.fasciaColumns.get(Fascia.GIORNO_SETTIMANA), _giornoSettimana.getText().toString());

        List<Row> rows = ConcreteDataAccessor.getInstance().read(Fascia.TABLE_NAME, null, selectionRow, null);
        boolean result = true;
        for (Row row : rows) {
            if (Float.parseFloat(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO)).toString()) > Float.parseFloat(_oraInizio.getText().toString()) &&
                Float.parseFloat(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO)).toString()) < Float.parseFloat(_oraFine.getText().toString())) {
                result = false;
                break;
            }
            else if (Float.parseFloat(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_FINE)).toString()) > Float.parseFloat(_oraInizio.getText().toString()) &&
                     Float.parseFloat(row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_FINE)).toString()) < Float.parseFloat(_oraFine.getText().toString())) {
                result = false;
                break;
            }

        }
        return result;
    }

    public void makeAnnulla() {
        _descrizione.setText("");
        _oraInizio.setText("");
        _oraFine.setText("");
        _giornoSettimana.setText("");
        _capienza.setText("");
        makeToastMessage(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito").show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(NuovaFascia.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
