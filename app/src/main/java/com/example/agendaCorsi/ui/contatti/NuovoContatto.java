package com.example.agendaCorsi.ui.contatti;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.corsi.NuovoCorso;
import com.example.agendacorsi.R;

import java.util.Calendar;

public class NuovoContatto extends FunctionBase {

    TextView nome, indirizzo, telefono, email;
    EditText dataNascita;
    Context nuovoContatto;
    final Calendar myCalendar= Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_contatto);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        nome = findViewById(R.id.editNome);
        indirizzo = findViewById(R.id.editIndirizzo);
        telefono = findViewById(R.id.editTelefono);
        email = findViewById(R.id.editEmail);

        /**
         * Date picker data inizio validità
         */
        dataNascita = findViewById(R.id.editDataNascita);

        DatePickerDialog.OnDateSetListener dataNasc = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(dataNascita, myCalendar);
            }
        };
        dataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NuovoContatto.this, dataNasc, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        nuovoContatto = this;
        /*
         * implemento i listener sui bottoni
         */
        listenerEsci(nuovoContatto, ElencoContatti.class, null);
        listenerAnnulla();
        listenerSalva();
    }

    public void makeSalva() {
        Contatto contatto = new Contatto(null, nome.getText().toString(),
                dateFormat(dataNascita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),
                indirizzo.getText().toString(),
                telefono.getText().toString(),
                email.getText().toString(),
                null);
        /**
         * controllo validità campi inseriti
         */
        if (contatto.getNome().equals("") ||
            contatto.getDataNascita().equals("") ||
            contatto.getTelefono().equals("")) {

            displayAlertDialog(nuovoContatto, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            if (ContattiDAO.getInstance().insert(contatto, QueryComposer.getInstance().getQuery(QUERY_INS_CONTATTO))) {
                Toast.makeText(AgendaCorsiApp.getContext(), "Contatto creato con successo.", Toast.LENGTH_LONG).show();
                esci.callOnClick();
            }
            else {
                displayAlertDialog(nuovoContatto, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
            }
        }
    }
}
