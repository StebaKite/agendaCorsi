package com.example.agendaCorsi.ui.corsi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendacorsi.R;

import java.util.Calendar;
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
                mTimePicker = new TimePickerDialog(ModificaFascia.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        _oraFine.setText(selectedHour + "." + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
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
        /*
         * Carico i dati della fascia selezionata
         */
        Fascia fascia = new Fascia(String.valueOf(idFascia), String.valueOf(idCorso),null,null, null, null, null,null,null);
        FasciaDAO.getInstance().select(fascia, QueryComposer.getInstance().getQuery(QUERY_GET_FASCIA));

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
            giornoSettimana = _giornoSettimana.getText().toString();
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
        makeToastMessage(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito").show();
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
            if (isNumberOfWeek(Integer.parseInt(fascia.getGiornoSettimana()))) {
                if (FasciaDAO.getInstance().isNew(fascia, QueryComposer.getInstance().getQuery(QUERY_ISNEW_FASCIA))) {
                    if (Integer.parseInt(String.valueOf(_capienza.getText())) >= Integer.parseInt(capienza)) {
                        if (FasciaDAO.getInstance().update(fascia, QueryComposer.getInstance().getQuery(QUERY_MOD_FASCIA))) {
                            makeToastMessage(AgendaCorsiApp.getContext(), "Fascia aggiornata con successo.").show();
                            esci.callOnClick();
                        }
                        else {
                            displayAlertDialog(modificaFascia, "Attenzione!", "Modifica fallita, contatta il supporto tecnico");
                        }
                    } else {
                        displayAlertDialog(modificaFascia, "Attenzione!", "Riduzione capienza fascia non permessa");
                    }
                }
                else {
                    displayAlertDialog(modificaFascia, "Attenzione!", "Fascia sovrapposta non ammessa");
                }
            }
            else {
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
